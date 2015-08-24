package newegg.ec.warden.watch.hbase;

import com.newegg.ec.warden.watch.BaseWatch;
import com.newegg.ec.warden.watch.LogTimeReporter;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.pathsystem.PathConstructType;
import newegg.ec.disnotice.tool.dynamicconf.DynamicConfigurationFactory;
import newegg.ec.disnotice.tool.dynamicconf.validator.IntegerValueValidator;
import newegg.ec.disnotice.tool.zk.ZKConnections;
import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.WardenLogging;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by wz68 on 2015/7/9.
 * <p/>
 * monitor regions from specify HBase table
 */
public class HBaseCDH430RegionWatch extends BaseWatch implements HBaseMonitorConstants {
    private static Logger log = WardenLogging.getLog(HBaseCDH430RegionWatch.class);
    private static final Integer default_hbase_pool_size = 32;
    // cache it , init for first
    protected List<String> rowKeyList = new ArrayList<String>();
    private String tableName;
    private Configuration hbaseConf;
    private HTablePool pool;
    private HBaseWatchMonitorReporter hbaseWatchMonitorReporter;
    private DynamicConfigurationFactory dynamicConfigurationFactory;


    public PropertyLoader getPropLoader() {
        return propLoader;
    }

    public void setPropLoader(PropertyLoader propLoader) {
        this.propLoader = propLoader;
    }

    public HBaseCDH430RegionWatch(PropertyLoader propLoader) {
        super(propLoader);
        try {
            initAndStartDynamicConf();
            init();
            subThreadsStart();
        } catch (Exception e) {
            log.error("init HBaseCDH430RegionWatch failed,should exit", e);
            System.exit(-1);
        }
    }

    private void initAndStartDynamicConf() throws Exception {
        dynamicConfigurationFactory = DynamicConfigurationFactory.getDynamicConfigurationInstance();
        String zkServers = this.propLoader.getValue(DISNOTICE_ZK_CONNECT_SERVERS);

        // dynamic conf init
        InstanceConfiguration inc = new InstanceConfiguration(
                PathConstructType.InstancePathType.conf,
                PathConstructType.InstancePathScope.app,
                propLoader.getValue(WATCH_INSTANCE_NAME));
        String nodeMapRootPath = DisPathManager.getInstancePath(inc);

        dynamicConfigurationFactory.init(ZKConnections.getZKConnectionsInstance().get(zkServers), nodeMapRootPath);
        // watch.timeout.millisecond
        Integer defaultWatchTimeOutSetting = Integer.parseInt(this.propLoader.getValue(WATCH_TIMEOUT_MILLISECOND, "1000"));
        dynamicConfigurationFactory.initKeyValueWithCreateIfNotExist(WATCH_TIMEOUT_MILLISECOND, defaultWatchTimeOutSetting + "");
        dynamicConfigurationFactory.registerValidatorListenerAndSync(WATCH_TIMEOUT_MILLISECOND, new IntegerValueValidator());

        // watch.interval.seconds
        Integer defaultWatchInterruptSeconds = Integer.parseInt(propLoader.getValue(WATCH_INTERVAL_SECONDS, "3"));
        dynamicConfigurationFactory.initKeyValueWithCreateIfNotExist(WATCH_INTERVAL_SECONDS, defaultWatchInterruptSeconds + "");
        dynamicConfigurationFactory.registerValidatorListenerAndSync(WATCH_INTERVAL_SECONDS, new IntegerValueValidator());

        // key.cache.update.interval.seconds
        Integer defaultKeyCacheUpdateSeconds = Integer.parseInt(propLoader.getValue(KEY_CACHE_UPDATE_INTERVAL_SECONDS, "10"));
        dynamicConfigurationFactory.initKeyValueWithCreateIfNotExist(KEY_CACHE_UPDATE_INTERVAL_SECONDS, defaultKeyCacheUpdateSeconds + "");
        dynamicConfigurationFactory.registerValidatorListenerAndSync(KEY_CACHE_UPDATE_INTERVAL_SECONDS, new IntegerValueValidator());

        // async.task.processor.timeout.seconds
        Integer defaultAsyncTaskProcessorTimeoutSeconds = Integer.parseInt(propLoader.getValue(ASYNC_TASK_PROCESSOR_TIMEOUT_SECONDS, "10"));
        dynamicConfigurationFactory.initKeyValueWithCreateIfNotExist(ASYNC_TASK_PROCESSOR_TIMEOUT_SECONDS, defaultAsyncTaskProcessorTimeoutSeconds + "");
        dynamicConfigurationFactory.registerValidatorListenerAndSync(ASYNC_TASK_PROCESSOR_TIMEOUT_SECONDS, new IntegerValueValidator());
    }

    public void init() throws Exception {

        Configuration conf = new Configuration();
        log.info("load hbase conf name = " + this.propLoader.getValue("hbase.conf.name"));
        conf.addResource(this.propLoader.getValue("hbase.conf.name"));
        hbaseConf = HBaseConfiguration.create(conf);
        this.tableName = propLoader.getValue("watch.table.Name");
        this.pool = new HTablePool(hbaseConf, default_hbase_pool_size);
        this.hbaseWatchMonitorReporter = new HBaseWatchMonitorReporter(this.propLoader);
    }

    private void subThreadsStart() throws Exception {
        log.info("refresh-monitor start");
        new Thread(new RowKeyUpdateThread()).start();
    }

    public void updateRowKeyCache() {
        try {
            rowKeyList = getTableRegionSplits(hbaseConf, tableName);
        } catch (IOException e) {
            log.error("updateRowKeyCache failed,", e);
        }
    }

    @Override
    public void watch() {

        while (true) {
            try {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                log.info("watch start,table name = " + tableName);
                final List<Get> getList = new ArrayList<Get>();
                for (String rowKey : rowKeyList) {
                    getList.add(new Get(rowKey.getBytes()));
                }
                FutureTask<Long> watchTask =
                        new FutureTask<Long>(new Callable<Long>() {
                            public Long call() {
                                try {
                                    long startTime = System.currentTimeMillis();
                                    List<String> resultKeyList = new ArrayList<String>();
                                    HTableInterface hTable = pool.getTable(tableName);
                                    Result[] resultList = hTable.get(getList);
                                    for (Result rs : resultList) {
                                        byte[] rowkey = rs.getRow();
                                        if (null != rowkey) {
                                            resultKeyList.add(new String(rs.getRow(), "UTF-8"));
                                        }
                                    }
                                    long costTime = System.currentTimeMillis() - startTime;
                                    return costTime;
                                } catch (Throwable e) {
                                    log.error("watch task failed , ", e);
                                    return null;
                                }
                            }
                        });
                executor.execute(watchTask);
                try {
                    // timeout
                    Long timeCost = watchTask.get(Integer.parseInt(dynamicConfigurationFactory.get(ASYNC_TASK_PROCESSOR_TIMEOUT_SECONDS)) * 1000, TimeUnit.MILLISECONDS);
                    if (null == timeCost) {
                        throw new RuntimeException("get null value from task , watch task failed");
                    }
                    // overwrite the value
                    log.info("watch task success , timeCost=" + timeCost);
                    int cpCost = Integer.parseInt(dynamicConfigurationFactory.get(WATCH_TIMEOUT_MILLISECOND));
                    if (timeCost > cpCost) {
                        throw new RuntimeException("timeCost > " + cpCost);
                    }
                    // not exception
                    hbaseWatchMonitorReporter.setTableStatus(this.tableName, true);
                } catch (Throwable t) {
                    hbaseWatchMonitorReporter.setTableStatus(this.tableName, false);
                    log.error("watch task failed,set hbase status false,the reason is:", t);
                } finally {
                    watchTask.cancel(true);
                    executor.shutdown();
                    log.info("watch task over");
                    logTimeReporter.logTime();
                }
            } catch (Throwable t) {
                log.error("watch task failed,", t);
            }
            try {
                Thread.sleep(Integer.parseInt(dynamicConfigurationFactory.get(WATCH_INTERVAL_SECONDS)) * 1000);
            } catch (InterruptedException e) {
                log.error("sleep error", e);
            }
        }
    }


    public static List<String> getTableRegionSplits(Configuration conf, String tableName) throws IOException, UnsupportedEncodingException {
        HTable table = new HTable(conf, tableName);
        List<String> splitsList = new ArrayList<String>();
        List<ImmutableBytesWritable> startKeys = getRegionStartKeys(table);
        for (ImmutableBytesWritable key : startKeys) {
            if (key.get().length != 0)
                splitsList.add(new String(key.get(), "UTF-8"));
        }
        return splitsList;
    }

    /**
     * Return the start keys of all of the regions in this table, as a list of ImmutableBytesWritable.
     */
    private static List<ImmutableBytesWritable> getRegionStartKeys(HTable table) throws IOException {
        byte[][] byteKeys = table.getStartKeys();
        ArrayList<ImmutableBytesWritable> ret = new ArrayList<ImmutableBytesWritable>(byteKeys.length);
        for (byte[] byteKey : byteKeys) {
            ret.add(new ImmutableBytesWritable(byteKey));
        }
        return ret;
    }

    class RowKeyUpdateThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                log.info("refresh rowkeylist task start...");
                ExecutorService executor = Executors.newSingleThreadExecutor();
                FutureTask<List<String>> futureTask =
                        new FutureTask<List<String>>(new Callable<List<String>>() {
                            public List<String> call() {
                                try {
                                    log.info("start to refresh ");
                                    rowKeyList = getTableRegionSplits(hbaseConf, tableName);
                                    return rowKeyList;
                                } catch (IOException e) {
                                    log.error("update rowkey failed", e);
                                    return null;
                                }
                            }
                        });
                executor.execute(futureTask);
                try {
                    // timeout
                    List<String> result = futureTask.get(Long.parseLong(dynamicConfigurationFactory.get(ASYNC_TASK_PROCESSOR_TIMEOUT_SECONDS)) * 1000, TimeUnit.MILLISECONDS);
                    if (null == result) {
                        throw new RuntimeException("get null value from task");
                    }
                    // overwrite the value
                    rowKeyList = result;
                    log.info("refresh rowkeylist success");
                    for (String str : rowKeyList) {
                        log.debug("refresh rowkey = " + str);
                    }
                } catch (Throwable t) {
                    log.error("refresh rowkey task failed,the reason is:", t);
                } finally {
                    futureTask.cancel(true);
                    executor.shutdown();
                }

                try {
                    Thread.sleep(Integer.parseInt(dynamicConfigurationFactory.get(KEY_CACHE_UPDATE_INTERVAL_SECONDS)) * 1000);
                } catch (InterruptedException e) {
                    log.error("sleep error", e);
                }
            }
        }
    }

    public static void main(String[] args) {
        log.info("start hbaseCDH430RegionWatch ... ");
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        HBaseCDH430RegionWatch hbaseCDH430RegionWatch = (HBaseCDH430RegionWatch) context.getBean("hbaseCDH430RegionWatch");
        hbaseCDH430RegionWatch.watch();
    }
}



