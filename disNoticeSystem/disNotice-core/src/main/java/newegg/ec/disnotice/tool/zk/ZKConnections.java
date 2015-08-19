package newegg.ec.disnotice.tool.zk;


import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.core.zk.ZKManagerAsCurator;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wz68 on 2015/8/1.
 * <p/>
 * <p/>
 * TODO these implement is too bad , should be mo grace
 * &
 */
public class ZKConnections {

    public static ZKConnections getZKConnectionsInstance() {
        return ZKConnectionsHandler.zkConnections;
    }

    /**
     * key is ZKServers
     */
    public Map<String, ZKManager> zkConnectionPool = new ConcurrentHashMap<String, ZKManager>();

    private static class ZKConnectionsHandler {
        private static ZKConnections zkConnections;

        static {
            zkConnections = new ZKConnections();
        }
    }

    public ZKConnections() {
    }

    // TODO synchronized is not prefect
    public synchronized ZKManager get(String zkServers) throws Exception {
        ZKManager zkmgr = zkConnectionPool.get(zkServers);
        if (null == zkmgr) {
            zkmgr = new ZKManagerAsCurator(zkServers);
            zkConnectionPool.put(zkServers, zkmgr);
            zkmgr.initAndStart();
        }
        return zkmgr;
    }

    public void close(String zkServers) throws Exception {
        ZKManager zkmgr = zkConnectionPool.get(zkServers);
        if (null != zkmgr) {
            zkmgr.close();
            zkConnectionPool.remove(zkServers);
        }
    }

    public synchronized void closeAll() throws Exception {
        Iterator<Map.Entry<String, ZKManager>> it = zkConnectionPool.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ZKManager> entry = it.next();
            String key = entry.getKey();
            ZKManager zkMgr = entry.getValue();
            zkMgr.close();
            zkConnectionPool.remove(key);
        }
    }

}
