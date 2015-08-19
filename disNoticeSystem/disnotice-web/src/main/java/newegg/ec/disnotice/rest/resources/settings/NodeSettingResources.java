package newegg.ec.disnotice.rest.resources.settings;

import newegg.ec.disnotice.business.dao.util.IdWorker;
import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import newegg.ec.disnotice.business.dto.NodeSettingDTO;
import newegg.ec.disnotice.datainfo.DisNoticeDataType;
import newegg.ec.disnotice.rest.DefineMsg;
import newegg.ec.disnotice.rest.model.*;
import newegg.ec.disnotice.rest.resources.BaseResources;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wz68
 */
@Path("settings/node")
public class NodeSettingResources extends BaseResources {
    private static Logger log = WardenLogging.getLog(NodeSettingResources.class);


    @GET
    @Path("getall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public NodeSettingAllModel getAllNodeSettings() {
        NodeSettingAllModel nsam = new NodeSettingAllModel();
        Set<NodeSettingDTO> nodeSettingDTOs = nodeSettingDAO.getAllNode();
        List<NodeSettingModel> nodeSettingModelList = nodeDTOToModelParseCollections(nodeSettingDTOs);
        nsam.setNodeList(nodeSettingModelList);
        return nsam;
    }

    @POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel updateNodeSetting(NodeSettingModel nodeSettingModel) {
        try {
            NodeSettingDTO nodeSettingDTO = nodeModelToDTOParse(nodeSettingModel);
            nodeSettingDAO.saveTable(nodeSettingDTO);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel addNodeSetting(NodeSettingModel nodeSettingModel) {
        try {
            String nodeName = nodeSettingModel.getNodeName();
            if (nodeSettingDAO.existNodeName(nodeName.trim())) {
                return new ReturnMessageModel(DefineMsg.FAILED_MSG + ", node name is exist , please try different name", DefineMsg.FAILED);
            }
            NodeSettingDTO nodeSettingDTO = nodeModelToDTOParse(nodeSettingModel);
            nodeSettingDTO.setNodeType(DisNoticeDataType.MapData.MAP_DATA);
            nodeSettingDTO.setNodeID(IdWorker.getNodeID());
            nodeSettingDAO.saveTable(nodeSettingDTO);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel deleteNodeSetting(NodeSettingModel nodeSettingModel) {
        try {
            String nodeID = nodeSettingModel.getNodeID();
            nodeSettingDAO.deleteNodeByID(nodeID);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }
}
