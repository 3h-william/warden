package newegg.ec.disnotice.storage;

import newegg.ec.disnotice.business.dto.ZKSettingDTO;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.core.zk.ZKManagerAsCurator;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wz68 on 2015/7/23.
 * <p/>
 * maintain all zk connection manager in this class
 */
public class ZKConnStorage {
    private static Logger log = WardenLogging.getLog(ZKConnStorage.class);
    private Map<String, ZKManager> zkStorageMap = new ConcurrentHashMap<String, ZKManager>();

    private static class ZKStorageInstance {
        public static ZKConnStorage zkConnectionStorageInstance;

        static {
            zkConnectionStorageInstance = new ZKConnStorage();
        }
    }

    public static ZKConnStorage getZKStorageInstance() {
        return ZKStorageInstance.zkConnectionStorageInstance;
    }

    public void createConnectionAndStore(ZKSettingDTO zkSettingDTO) throws Throwable {
        try {
            log.info("start to connection zk servers : " + zkSettingDTO.getZkServers());
            // check if exist
            if (zkStorageMap.containsKey(zkSettingDTO.getZkID())) {
                throw new Exception("zk has been create");
            }
            ZKManager zkmgr = new ZKManagerAsCurator(zkSettingDTO.getZkServers());
            zkmgr.initAndStart();
            this.zkStorageMap.put(zkSettingDTO.getZkID(), zkmgr);
            log.info("success connection  zk servers : " + zkSettingDTO.getZkServers());
        } catch (Throwable t) {
            log.error("failed conntction zk servers:" + zkSettingDTO.getZkServers(), t);
            throw t;
        }
    }

    public ZKManager getZKManager(String zkID) {
        return zkStorageMap.get(zkID);
    }

    public boolean isZKConnect(String zkID) {
        return zkStorageMap.containsKey(zkID);
    }

    public void disZKConnect(String zkID) throws Exception {
        ZKManager zkManager = zkStorageMap.get(zkID);
        zkManager.close();
        zkStorageMap.remove(zkID);
    }
    //ZKManager zkmgr = new ZKManagerAsCurator("echadoop08:2181");
}
