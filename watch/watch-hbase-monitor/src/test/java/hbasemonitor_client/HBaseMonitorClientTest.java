package hbasemonitor_client;

import newegg.ec.disnotice.core.NodeEvent;
import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.collections.DisNoticeMapChangedListener;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.pathsystem.PathConstructType;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.core.zk.ZKManagerAsCurator;
import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.watch.hbase.HBaseMonitorConstants;
import newegg.ec.warden.watch.hbase.HBaseWatchMonitorReporter;

import java.util.Date;

/**
 * Created by wz68 on 2015/7/14.
 */
public class HBaseMonitorClientTest implements HBaseMonitorConstants {
    static PropertyLoader propertyLoader = new PropertyLoader("hbase430.properties");

    public static final String watchTableName = propertyLoader.getValue("watch.table.Name");

    public static void main(String[] args) throws Exception {


        // init
        InstanceConfiguration inc = new InstanceConfiguration(
                PathConstructType.InstancePathType.status,
                PathConstructType.InstancePathScope.cluster,
                propertyLoader.getValue(WATCH_REPORT_NAME));
        String mapRootPath = DisPathManager.getInstancePath(inc);
        System.out.print("init base watch monitor report zk path = " + mapRootPath);
        ZKManager zkmgr = new ZKManagerAsCurator(propertyLoader.getValue(HBaseWatchMonitorReporter.DISNOTICE_ZK_CONNECT_SERVERS));
        zkmgr.initAndStart();
        // map operations examples:
        DisNoticeMap dnm = new DisNoticeMap<String>(zkmgr, mapRootPath, String.class, DisNoticeMap.DisNoticeMapModel.READ);
        dnm.addDataChangedListener(new HBaseMonitorClientDataChangedListener(dnm));
        System.out.println("value changed : " + dnm.get(watchTableName));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class HBaseMonitorClientDataChangedListener implements DisNoticeMapChangedListener {
        public DisNoticeMap<String> disNoticeMap;

        public HBaseMonitorClientDataChangedListener(DisNoticeMap disNoticeMap) {
            this.disNoticeMap = disNoticeMap;
        }

        @Override
        public void dataChangedEvent(DisNoticeMap disNoticeMap, String ChangeKeyName, NodeEvent.Type nodeEventType) {
            System.out.println(new Date() + ",value changed : " + disNoticeMap.get(watchTableName));
        }
    }

}
