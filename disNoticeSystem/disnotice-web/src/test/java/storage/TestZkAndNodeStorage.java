package storage;

import newegg.ec.disnotice.business.dao.util.IdWorker;
import newegg.ec.disnotice.business.dto.NodeSettingDTO;
import newegg.ec.disnotice.business.dto.ZKSettingDTO;
import newegg.ec.disnotice.datainfo.DisNoticeDataType;
import newegg.ec.disnotice.storage.NodeDataStorage;
import newegg.ec.disnotice.storage.ZKConnStorage;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by wz68 on 2015/7/23.
 */
public class TestZkAndNodeStorage {
    public static void main(String[] args) throws Throwable {
        ZKConnStorage zkConnStorageInstance = ZKConnStorage.getZKStorageInstance();
        NodeDataStorage nodeDataStorageInstance = NodeDataStorage.getNodeStorageInstance();

        ZKSettingDTO zkSettingDTO = new ZKSettingDTO();
        zkSettingDTO.setZkID(IdWorker.getZKID());
        zkSettingDTO.setZkName("test zk ");
        zkSettingDTO.setZkServers("echadoop03:2181");

        // zk operate
        zkConnStorageInstance.createConnectionAndStore(zkSettingDTO);


        NodeSettingDTO nodeSettingDTO = new NodeSettingDTO();
        nodeSettingDTO.setNodeID(IdWorker.getNodeID());
        nodeSettingDTO.setNodeName("test node");
        nodeSettingDTO.setNodePath("/disRoot/instanceTest/mytest");
        nodeSettingDTO.setNodeType(DisNoticeDataType.MapData.MAP_DATA);
        nodeSettingDTO.setZkID(zkSettingDTO.getZkID());

        // node operate
        nodeDataStorageInstance.watchNodeData(nodeSettingDTO);
        Map<String, String> mapData = nodeDataStorageInstance.getAllMapDataFromNode(nodeSettingDTO.getNodeID());

        // change data
        nodeDataStorageInstance.putOrModifyMapDataFromNode(nodeSettingDTO.getNodeID(), "a", "aa11");

        // print data
        Iterator<Map.Entry<String, String>> i = mapData.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, String> entry = i.next();
            System.out.println("key=" + entry.getKey() + ",value=" + entry.getValue());
        }
        //endregion


    }
}
