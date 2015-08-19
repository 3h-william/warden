package newegg.ec.disnotice.storage;

import newegg.ec.disnotice.business.dto.NodeSettingDTO;
import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wz68 on 2015/7/23.
 */
public class NodeDataStorage {

    private static Logger log = WardenLogging.getLog(NodeDataStorage.class);
    private Map<String, Object> nodeDataStorage = new ConcurrentHashMap<String, Object>();

    private static class NodeStorageInstance {
        public static NodeDataStorage nodeDataStorageInstance;

        static {
            nodeDataStorageInstance = new NodeDataStorage();
        }
    }

    public static NodeDataStorage getNodeStorageInstance() {
        return NodeStorageInstance.nodeDataStorageInstance;
    }

    public void watchNodeData(NodeSettingDTO nodeSettingDTO) throws Throwable {
        log.info("start to watch node , node = " + nodeSettingDTO.getNodeName());
        ZKManager zkManager = ZKConnStorage.getZKStorageInstance().getZKManager(nodeSettingDTO.getZkID());
        if (null == zkManager) {
            throw new Exception("zk is not create,should  create first");
        }
        DisNoticeMap<String> dnm = new DisNoticeMap<String>(zkManager, nodeSettingDTO.getNodePath(), String.class, DisNoticeMap.DisNoticeMapModel.RW);
        nodeDataStorage.put(nodeSettingDTO.getNodeID(), dnm);
    }

    public void unwatchNodeData(String nodeID) throws Exception {
        DisNoticeMap<String> dnm = (DisNoticeMap<String>) nodeDataStorage.remove(nodeID);
        if(null!=dnm){
            log.info("close disNoticdMap");
            dnm.close();
        }
    }


    public Map<String, String> getAllMapDataFromNode(String nodeID) {
        Map<String, String> mapData = (Map<String, String>) nodeDataStorage.get(nodeID);
        return mapData;
    }

    public void putOrModifyMapDataFromNode(String nodeID,String key,String value){
        Map<String, String> mapData = (Map<String, String>) nodeDataStorage.get(nodeID);
        mapData.put(key,value);
    }

    public void nodeDataDelete(String nodeID,String key){
        Map<String, String> mapData = (Map<String, String>) nodeDataStorage.get(nodeID);
        mapData.remove(key);
    }

    public boolean isNodeWatch(String nodeID){
        return nodeDataStorage.containsKey(nodeID);
    }
}
