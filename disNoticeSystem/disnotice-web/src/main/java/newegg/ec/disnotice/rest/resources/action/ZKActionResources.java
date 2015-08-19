package newegg.ec.disnotice.rest.resources.action;

import newegg.ec.disnotice.business.dao.util.IdWorker;
import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import newegg.ec.disnotice.business.dto.NodeSettingDTO;
import newegg.ec.disnotice.business.dto.ZKSettingDTO;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.rest.DefineMsg;
import newegg.ec.disnotice.rest.model.GroupSettingModel;
import newegg.ec.disnotice.rest.model.NodeSettingModel;
import newegg.ec.disnotice.rest.model.ReturnMessageModel;
import newegg.ec.disnotice.rest.model.ZKSettingModel;
import newegg.ec.disnotice.rest.resources.BaseResources;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

/**
 * metrics request processor in server
 *
 * @author wz68
 */
@Path("action/zk")
public class ZKActionResources extends BaseResources {
    private static Logger log = WardenLogging.getLog(ZKActionResources.class);

    @POST
    @Path("connect")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel connectZK(ZKSettingModel zkSettingModel) {
        try {
            ZKSettingDTO zkSettingDTO = zkSettingDAO.getZKByID(zkSettingModel.getZkID());
            zkConnStorageInstance.createConnectionAndStore(zkSettingDTO);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            log.error("", t);
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("disConnect")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel disConnectZK(ZKSettingModel zkSettingModel) {
        try {
            // validator
            if (!zkConnStorageInstance.isZKConnect(zkSettingModel.getZkID())) {
                return new ReturnMessageModel("this zk id is not active," + DefineMsg.FAILED_MSG, DefineMsg.OK);
            }

            // check all nodes of this zkID is not watched
            Set<NodeSettingDTO> nodeSettingDTOSet = nodeSettingDAO.getNodeByZKID(zkSettingModel.getZkID());
            for (NodeSettingDTO nodeSettingDTO : nodeSettingDTOSet) {
                String nodeID = nodeSettingDTO.getNodeID();
                if (nodeDataStorageInstance.isNodeWatch(nodeID)) {
                    return new ReturnMessageModel("nodeID=" + nodeID + " , has been watched , should unwatch first!"
                            + DefineMsg.FAILED_MSG, DefineMsg.OK);
                }
            }
            // disConnectZK
            zkConnStorageInstance.disZKConnect(zkSettingModel.getZkID());
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            log.error("", t);
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }


}
