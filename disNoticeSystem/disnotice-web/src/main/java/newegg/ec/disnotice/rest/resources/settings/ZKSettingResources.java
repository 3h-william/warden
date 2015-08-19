package newegg.ec.disnotice.rest.resources.settings;

import newegg.ec.disnotice.business.dao.util.IdWorker;
import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import newegg.ec.disnotice.business.dto.ZKSettingDTO;
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
@Path("settings/zk")
public class ZKSettingResources extends BaseResources {
    private static Logger log = WardenLogging.getLog(ZKSettingResources.class);


    @GET
    @Path("getall")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ZKSettingAllModel getAllZKSettings() {
        ZKSettingAllModel zsam = new ZKSettingAllModel();
        List<ZKSettingModel> zkSettingModelList = new ArrayList<ZKSettingModel>();
        Set<ZKSettingDTO> zkSettingDTOSet = zkSettingDAO.getAllZK();
        for (ZKSettingDTO zdto : zkSettingDTOSet) {
            ZKSettingModel zsm = new ZKSettingModel();
            zsm.setZkID(zdto.getZkID());
            zsm.setZkName(zdto.getZkName());
            zsm.setZkServers(zdto.getZkServers());
            // connect stat
            zsm.setConnect(zkConnStorageInstance.isZKConnect(zsm.getZkID()));
            zkSettingModelList.add(zsm);
        }
        zsam.setZkList(zkSettingModelList);
        return zsam;
    }

    @POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel updateZKSetting(ZKSettingModel zkSettingModel) {
        try {
            ZKSettingDTO zkSettingDTO = zkModelToDTOParse(zkSettingModel);
            zkSettingDAO.saveTable(zkSettingDTO);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel addZKSetting(ZKSettingModel zkSettingModel) {
        try {
            String zkName = zkSettingModel.getZkName();
            if (zkSettingDAO.existZKName(zkName.trim())) {
                return new ReturnMessageModel(DefineMsg.FAILED_MSG + ", zk name is exist , please try different name", DefineMsg.FAILED);
            }
            ZKSettingDTO zkSettingDTO = new ZKSettingDTO();
            zkSettingDTO.setZkID(IdWorker.getZKID());
            zkSettingDTO.setZkName(zkSettingModel.getZkName().trim());
            zkSettingDTO.setZkServers(zkSettingModel.getZkServers().trim());
            zkSettingDAO.saveTable(zkSettingDTO);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel deleteGroupSetting(ZKSettingModel zkSettingModel) {
        try {
            String zkID = zkSettingModel.getZkID();
            zkSettingDAO.deleteZKByID(zkID);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }
}
