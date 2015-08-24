package examples.collections;

import newegg.ec.disnotice.core.NodeEvent;
import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.collections.DisNoticeMapChangedListener;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.pathsystem.PathConstructType;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.core.zk.ZKManagerAsCurator;

/**
 * Created by wz68 on 2015/7/11.
 */
public class DisNoticeMapTest {
    public static void main(String[] args) throws Exception {
        //PropertyLoader propLoader = new PropertyLoader("zk_test.properties");
        // init sample 1

        /**
         * @param $instancePathType = conf
         * @param $instancePathScope = app
         * @param $instanceName = mytest
         *
         */
        InstanceConfiguration inc = new InstanceConfiguration(
                PathConstructType.InstancePathType.conf,
                PathConstructType.InstancePathScope.app,
                "mytest");
        String mapRootPath = DisPathManager.getInstancePath(inc);

        // define path explicitly
        //String mapRootPath = "/disRoot/instanceTest/mytest";

        System.out.println("data path = " + mapRootPath);
        ZKManager zkmgr = new ZKManagerAsCurator("echadoop08:2181");
        zkmgr.initAndStart();
        //zkmgr.deleteIfExist(mapRootPath);

        // map operations examples:
        DisNoticeMap<String> dnm = new DisNoticeMap<String>(zkmgr, mapRootPath, String.class, DisNoticeMap.DisNoticeMapModel.RW);
        // addlistener (optional)
        dnm.addDataChangedListener(new DataChangedListener(dnm));

        // put test
        dnm.put("a", "aaa");
        dnm.put("b", "bbb");
        dnm.remove("a");
        dnm.put("a", "aaa");

        // get data
        Thread.sleep(3000);
        System.out.println(dnm.get("a"));
        System.out.println(dnm.get("b"));
        dnm.close();
    }
}

/**
 * Customer listener
 */
class DataChangedListener implements DisNoticeMapChangedListener {
    public DisNoticeMap disNoticeMap;

    public DataChangedListener(DisNoticeMap disNoticeMap) {
        this.disNoticeMap = disNoticeMap;
    }

    @Override
    public void dataChangedEvent(DisNoticeMap disNoticeMap, String ChangeKeyName, NodeEvent.Type nodeEventType) {
        System.out.println("changeKeyName= " + ChangeKeyName + ",eventType=" + nodeEventType + ",value=" + disNoticeMap.get(ChangeKeyName));
    }
}
