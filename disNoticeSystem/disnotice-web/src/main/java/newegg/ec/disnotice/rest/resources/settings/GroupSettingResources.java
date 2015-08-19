package newegg.ec.disnotice.rest.resources.settings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import newegg.ec.disnotice.business.dao.util.IdWorker;
import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import newegg.ec.disnotice.business.dto.NodeSettingDTO;
import newegg.ec.disnotice.rest.DefineMsg;
import newegg.ec.disnotice.rest.ResourceConstant;
import newegg.ec.disnotice.rest.model.*;
import newegg.ec.disnotice.rest.resources.BaseResources;
import newegg.ec.warden.WardenLogging;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author wz68
 */
@Path("settings/group")
public class GroupSettingResources extends BaseResources implements ResourceConstant {
    private static Logger log = WardenLogging.getLog(GroupSettingResources.class);


    @GET
    @Path("getall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GroupSettingAllModel getAllGroupSettings() {
        GroupSettingAllModel gsam = new GroupSettingAllModel();
        Set<GroupSettingDTO> groupSettingDTOSet = groupSettingDAO.getAllGroup();
        List<GroupSettingModel> groupModelList = new ArrayList<GroupSettingModel>();
        for (GroupSettingDTO gdto : groupSettingDTOSet) {
            GroupSettingModel gsm = new GroupSettingModel();
            gsm.setGroupID(gdto.getGroupID());
            gsm.setGroupName(gdto.getGroupName());
            groupModelList.add(gsm);
        }
        gsam.setGroupList(groupModelList);
        return gsam;
    }

    @POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel updateGroupSetting(GroupSettingModel groupSettingModel) {
        try {
            String groupName = groupSettingModel.getGroupName();
            if (groupName.length() > GROUP_NAME_MAX_LENGTH) {
                return new ReturnMessageModel(DefineMsg.FAILED_MSG + ", the length of group name is more than " + GROUP_NAME_MAX_LENGTH +
                        " characters , please try different name", DefineMsg.FAILED);
            }
            GroupSettingDTO groupSettingDTO = groupModelToDTOParse(groupSettingModel);
            groupSettingDAO.saveTable(groupSettingDTO);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel addGroupSetting(GroupSettingModel groupSettingModel) {
        try {
            String groupName = groupSettingModel.getGroupName();
            //validator
            if (groupSettingDAO.existGroupName(groupName.trim())) {
                return new ReturnMessageModel(DefineMsg.FAILED_MSG + ", group name is exist , please try different name", DefineMsg.FAILED);
            }
            if (groupName.length() > GROUP_NAME_MAX_LENGTH) {
                return new ReturnMessageModel(DefineMsg.FAILED_MSG + ", the length of group name is more than " + GROUP_NAME_MAX_LENGTH +
                        " characters , please try different name", DefineMsg.FAILED);
            }
            GroupSettingDTO groupSettingDTO = new GroupSettingDTO();
            groupSettingDTO.setGroupID(IdWorker.getGroupID());
            groupSettingDTO.setGroupName(groupName.trim());
            groupSettingDAO.saveTable(groupSettingDTO);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel deleteGroupSetting(GroupSettingModel groupSettingModel) {
        try {
            String groupID = groupSettingModel.getGroupID();
            groupSettingDAO.deleteGroupByID(groupID);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }


    @POST
    @Path("getGroupNodeMapping")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public GroupNodeSettingModel getGroupNodeSetting(GroupSettingModel groupSettingModel) throws Exception {
        GroupNodeSettingModel groupNodeSettingModel = new GroupNodeSettingModel();
        GroupSettingDTO groupSettingDTO = groupSettingDAO.getGroupByID(groupSettingModel.getGroupID());
        groupNodeSettingModel.setGroupID(groupSettingModel.getGroupID());

        // set selected nodes
        Set<String> nodes = groupSettingDTO.getNodes();
        Set<NodeSettingDTO> nodeSettingDTOSet = nodeSettingDAO.getNodeByIDs(groupSettingDTO.getNodes());
        groupNodeSettingModel.setSelectedNodeList(nodeDTOToModelParseCollections(nodeSettingDTOSet));

        // set unSelected nodes;
        Set<NodeSettingDTO> nodeSettingDTOSetAll = nodeSettingDAO.getAllNode();
        // remove selected items
        nodeSettingDTOSetAll.removeAll(nodeSettingDTOSet);
        groupNodeSettingModel.setUnSelectedNodeList(nodeDTOToModelParseCollections(nodeSettingDTOSetAll));
        return groupNodeSettingModel;
    }

}
