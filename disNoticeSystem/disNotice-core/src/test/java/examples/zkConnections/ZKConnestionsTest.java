package examples.zkConnections;

import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.tool.zk.ZKConnections;

/**
 * Created by wz68 on 2015/8/1.
 */
public class ZKConnestionsTest {
    public static void main(String[] args) throws Exception {

        ZKConnections zkConnections = ZKConnections.getZKConnectionsInstance();
        ZKManager zkManager = zkConnections.get("echadoop08:2181");

    }
}
