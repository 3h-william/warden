package newegg.ec.warden.watch.hbase;

import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.collections.DisNoticeMapChangedListener;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.core.zk.ZKManagerAsCurator;
import newegg.ec.disnotice.tool.dynamicconf.DynamicConfigurationFactory;
import newegg.ec.disnotice.tool.dynamicconf.validator.IntegerValueValidator;
import newegg.ec.disnotice.tool.zk.ZKConnections;
import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;
import sun.rmi.runtime.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class for record some status if HBase status changed.
 * Some Strategy is very important in this case.
 * <p/>
 * Created by wz68 on 2015/7/14.
 */
public class HBaseWatchMonitorReporter implements HBaseMonitorConstants {
    private static Logger log = WardenLogging.getLog(HBaseWatchMonitorReporter.class);

    // map
    public static final String WATCH_STATISTICS_MAP_NAME = "watch_statistics.map.name";
    // TODO concurrency control is not good in this condition
    public Map<String, Long> statusChangedLatency = new ConcurrentHashMap<String, Long>();
    private PropertyLoader propertyLoader;
    private DynamicConfigurationFactory dynamicConfigurationFactory;

    private DisNoticeMap<String> dnm = null;

    public HBaseWatchMonitorReporter(PropertyLoader propertyLoader) throws Exception {
        this.propertyLoader = propertyLoader;
        this.dynamicConfigurationFactory = DynamicConfigurationFactory.getDynamicConfigurationInstance();
        initDynamicConf();
        init();
    }

    private void initDynamicConf() {
        // status.changed.latency.seconds
        Integer defaultStatusChangedLatencySeconds = Integer.parseInt(propertyLoader.getValue(STATUS_CHANGED_LATENCY_SECONDS, "30"));
        dynamicConfigurationFactory.initKeyValue(STATUS_CHANGED_LATENCY_SECONDS, defaultStatusChangedLatencySeconds + "");
        dynamicConfigurationFactory.registerValidatorListenerAndSync(STATUS_CHANGED_LATENCY_SECONDS, new IntegerValueValidator());
    }

    private void init() throws Exception {
        // init
        InstanceConfiguration inc = new InstanceConfiguration(propertyLoader.getValue(WATCH_INSTANCE_NAME), "0.1");
        String mapRootPath = DisPathManager.getInstanceData(inc, propertyLoader.getValue(WATCH_STATISTICS_MAP_NAME));
        String zkServers = propertyLoader.getValue(DISNOTICE_ZK_CONNECT_SERVERS);
        ZKManager zkmgr = ZKConnections.getZKConnectionsInstance().get(zkServers);
        // map operations examples:
        this.dnm = new DisNoticeMap<String>(zkmgr, mapRootPath, String.class, DisNoticeMap.DisNoticeMapModel.RW);
    }


    /**
     * False = > True  should in some latency
     * True = > False should immediately
     *
     * @param tableName
     * @param status
     */
    public void setTableStatus(String tableName, boolean status) {
        String preStat = this.dnm.get(tableName);
        // if now is true
        if (status) {
            if (null != preStat && preStat.equalsIgnoreCase("True")) {
                // do nothing
            } else {
                // latency for changed status in this condition False=>True
                latencyForValueChanged(tableName, "True");
            }
        }
        // if now is false
        else {
            if (null != preStat && preStat.equalsIgnoreCase("False")) {
                // do nothing
            } else {
                this.dnm.put(tableName, "False");
            }
            recoverLatencyRecord(tableName);
        }
    }

    /**
     * if some value changed should be in some latency ,that it can use this function
     *
     * @param key
     * @param value
     * @return
     */
    private void latencyForValueChanged(String key, String value) {
        Long preRecord = statusChangedLatency.get(key);
        if (null == preRecord) {
            preRecord = System.currentTimeMillis();
            statusChangedLatency.put(key, preRecord);
            log.info("record " + key + "  , modify time for changed in latency");
        } else {
            Long latencyTime = Long.parseLong(dynamicConfigurationFactory.get(STATUS_CHANGED_LATENCY_SECONDS)) * 1000;
            Long nowTime = System.currentTimeMillis();
            // reach latency ,then change value & recover latency record
            if (latencyTime + preRecord < nowTime) {
                log.info(key + ", value changed , now value =" + value);
                this.dnm.put(key, value);
                recoverLatencyRecord(key);
            } else {
                log.info(key + ", waiting for changed in latency");
            }
        }
    }

    private void recoverLatencyRecord(String key) {
        log.info(key + " recover latency record");
        statusChangedLatency.remove(key);
    }
}


