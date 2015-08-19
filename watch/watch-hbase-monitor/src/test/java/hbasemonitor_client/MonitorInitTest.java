package hbasemonitor_client;

import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.tool.dynamicconf.validator.IntegerValueValidator;
import newegg.ec.disnotice.tool.zk.ZKConnections;
import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.watch.hbase.HBaseMonitorConstants;

/**
 * Created by wz68 on 2015/8/1.
 */
public class MonitorInitTest implements HBaseMonitorConstants{
    public static void main(String[] args) throws Exception {
        PropertyLoader propertyLoader = new PropertyLoader("hbase430.properties");

        // init
        InstanceConfiguration inc = new InstanceConfiguration(propertyLoader.getValue(WATCH_INSTANCE_NAME), "0.1");
        String mapRootPath = DisPathManager.getInstanceData(inc, "hbase-monitor-instance-conf");
        String zkServers = propertyLoader.getValue(DISNOTICE_ZK_CONNECT_SERVERS);
        ZKManager zkmgr = ZKConnections.getZKConnectionsInstance().get(zkServers);
        // map operations examples:
        DisNoticeMap<String> dnm = new DisNoticeMap<String>(zkmgr, mapRootPath, String.class, DisNoticeMap.DisNoticeMapModel.RW);

        dnm.put(WATCH_TIMEOUT_MILLISECOND,"1000");
        dnm.put(KEY_CACHE_UPDATE_INTERVAL_SECONDS,"10");
        dnm.put(WATCH_INTERVAL_SECONDS,"3");
        dnm.put(ASYNC_TASK_PROCESSOR_TIMEOUT_SECONDS,"10");
        dnm.put(STATUS_CHANGED_LATENCY_SECONDS,"50");

    }
}
