package zookeeper.zk;

import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.core.zk.ZKManagerAsCurator;
import newegg.ec.warden.PropertyLoader;

/**
 * Created by wz68 on 2015/7/10.
 */
public class ZKManagerTest {
    public static void main(String[] args) throws Exception {
        PropertyLoader propLoader = new PropertyLoader("zk_test.properties");
        ZKManager zkmgr = new ZKManagerAsCurator(propLoader);
        zkmgr.startZKConnection();
        zkmgr.startNodeListener("/mytest",new MyNodeListener());
        Thread.sleep(1000000);
    }
}
class MyNodeListener implements ZKManager.NodeListenerCallback {
    @Override
    public void valueChanged(String value) {
        System.out.println("valueChanged:"+value);
    }
}




