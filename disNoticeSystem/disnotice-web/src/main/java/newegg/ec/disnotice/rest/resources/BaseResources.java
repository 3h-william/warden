package newegg.ec.disnotice.rest.resources;

import newegg.ec.disnotice.business.dao.DAOInstance;
import newegg.ec.disnotice.business.dao.base.IGroupSettingDAO;
import newegg.ec.disnotice.business.dao.base.INodeSettingDAO;
import newegg.ec.disnotice.business.dao.base.IZKSettingDAO;
import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import newegg.ec.disnotice.business.dto.NodeSettingDTO;
import newegg.ec.disnotice.business.dto.ZKSettingDTO;
import newegg.ec.disnotice.rest.model.GroupSettingModel;
import newegg.ec.disnotice.rest.model.NodeSettingModel;
import newegg.ec.disnotice.rest.model.ReturnMessageModel;
import newegg.ec.disnotice.rest.model.ZKSettingModel;
import newegg.ec.disnotice.storage.NodeDataStorage;
import newegg.ec.disnotice.storage.ZKConnStorage;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wz68 on 2015/7/18.
 */
public abstract class BaseResources {
    protected IGroupSettingDAO groupSettingDAO = DAOInstance.getGroupSettingDAO();
    protected IZKSettingDAO zkSettingDAO = DAOInstance.getZKSettingDAO();
    protected INodeSettingDAO nodeSettingDAO = DAOInstance.getNodeSettingDAO();

    protected ZKConnStorage zkConnStorageInstance = ZKConnStorage.getZKStorageInstance();
    protected NodeDataStorage nodeDataStorageInstance = NodeDataStorage.getNodeStorageInstance();


    protected ZKSettingDTO zkModelToDTOParse(ZKSettingModel zkSettingModel) {
        ZKSettingDTO zkSettingDTO = new ZKSettingDTO();
        zkSettingDTO.setZkID(zkSettingModel.getZkID());
        zkSettingDTO.setZkServers(zkSettingModel.getZkServers());
        zkSettingDTO.setZkName(zkSettingModel.getZkName());
        return zkSettingDTO;
    }

    protected NodeSettingDTO nodeModelToDTOParse(NodeSettingModel nodeSettingModel) {
        NodeSettingDTO nodeSettingDTO = new NodeSettingDTO();
        nodeSettingDTO.setNodeID(nodeSettingModel.getNodeID());
        nodeSettingDTO.setNodeName(nodeSettingModel.getNodeName());
        nodeSettingDTO.setNodePath(nodeSettingModel.getNodePath());
        nodeSettingDTO.setNodeType(nodeSettingModel.getNodeType());
        nodeSettingDTO.setZkID(nodeSettingModel.getZkID());
        nodeSettingDTO.setExtraInfo(nodeSettingModel.getExtraInfoStr());
        return nodeSettingDTO;
    }

    protected NodeSettingModel nodeDTOToModelParse(NodeSettingDTO ndto) {
        NodeSettingModel nodeSettingModel = new NodeSettingModel();
        nodeSettingModel.setNodeID(ndto.getNodeID());
        nodeSettingModel.setNodeName(ndto.getNodeName());
        nodeSettingModel.setNodePath(ndto.getNodePath());
        nodeSettingModel.setNodeType(ndto.getNodeType());
        nodeSettingModel.setZkID(ndto.getZkID());
        nodeSettingModel.setExtraInfoStr(ndto.getExtraInfo());
        nodeSettingModel.setWatch(nodeDataStorageInstance.isNodeWatch(ndto.getNodeID()));
        return nodeSettingModel;
    }

    protected List<NodeSettingModel> nodeDTOToModelParseCollections(Set<NodeSettingDTO> nodeSettingDTOs) {
        List<NodeSettingModel> nodeSettingModelList = new ArrayList<NodeSettingModel>();
        for (NodeSettingDTO ndto : nodeSettingDTOs) {
            NodeSettingModel nodeSettingModel = nodeDTOToModelParse(ndto);
            nodeSettingModelList.add(nodeSettingModel);
        }
        return nodeSettingModelList;
    }

    protected GroupSettingDTO groupModelToDTOParse(GroupSettingModel groupSettingModel) {
        GroupSettingDTO groupSettingDTO = new GroupSettingDTO();
        groupSettingDTO.setGroupID(groupSettingModel.getGroupID());
        groupSettingDTO.setGroupName(groupSettingModel.getGroupName().trim());
        groupSettingDTO.setNodes(groupSettingModel.getNodes());
        return groupSettingDTO;
    }


    protected ReturnMessageModel wrapThrowableToReturn(String msg, int code, Throwable t) {
        String msgR = msg;
        if(null!=t){
            msgR += ExceptionUtils.getStackTrace(t);
        }
        return new ReturnMessageModel(msgR, code);
    }

}
