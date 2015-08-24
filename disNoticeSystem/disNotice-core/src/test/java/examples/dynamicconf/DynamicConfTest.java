package examples.dynamicconf;

import newegg.ec.disnotice.core.NodeEvent;
import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.pathsystem.PathConstructType;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.core.zk.ZKManagerAsCurator;
import newegg.ec.disnotice.tool.dynamicconf.DynamicConfValidator;
import newegg.ec.disnotice.tool.dynamicconf.DynamicConfigurationFactory;

/**
 * Created by wz68 on 2015/8/1.
 */
public class DynamicConfTest {


    public static void main(String[] args) throws Exception {

        // init path
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
                "instanceTest");
        String mapRootPath = DisPathManager.getInstancePath(inc);

        // OR define path explicitly
        //String mapRootPath = "/disRoot/instanceTest/mytest";

        System.out.println("data path = " + mapRootPath);
        ZKManager zkmgr = new ZKManagerAsCurator("echadoop08:2181");
        zkmgr.initAndStart();


        // dynamic conf test
        DynamicConfigurationFactory dynamicConfigurationFactory = DynamicConfigurationFactory.getDynamicConfigurationInstance();
        dynamicConfigurationFactory.init(zkmgr, mapRootPath);
        dynamicConfigurationFactory.registerValidatorListenerAndSync("a", new nullValueValidator());
        dynamicConfigurationFactory.initKeyValueWithCreateIfNotExist("a", "initA"); // also can use initKeyValue
        System.out.println("get init value="+dynamicConfigurationFactory.get("a"));


        // test changed values

        DisNoticeMap<String> dnmWrite = new DisNoticeMap<String>(zkmgr, mapRootPath, String.class, DisNoticeMap.DisNoticeMapModel.WRITE);
        // put test
        dnmWrite.put("a", "aaa");
        dnmWrite.put("b", "bbb");
        dnmWrite.remove("a");
        dnmWrite.put("a", "null");


        Thread.sleep(1000);

        System.out.println(dynamicConfigurationFactory.get("a"));

    }
    static class nullValueValidator implements DynamicConfValidator {
        @Override
        public boolean validator(Object value, NodeEvent.Type nodeEventType) {
            String str = (String) value;
            if (null == value||( (String) value).equalsIgnoreCase("null") ) {
                System.out.println("validator false,value="+value);
                return false;
            }  System.out.println("validator true,value="+value);

            return true;
        }
    }

}
