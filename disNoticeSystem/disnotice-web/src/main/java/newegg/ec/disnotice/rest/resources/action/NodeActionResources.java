package newegg.ec.disnotice.rest.resources.action;

import newegg.ec.disnotice.business.dto.NodeSettingDTO;
import newegg.ec.disnotice.rest.DefineMsg;
import newegg.ec.disnotice.rest.model.*;
import newegg.ec.disnotice.rest.resources.BaseResources;
import newegg.ec.warden.WardenLogging;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * metrics request processor in server
 *
 * @author wz68
 */
@Path("action/node")
public class NodeActionResources extends BaseResources {
    private static Logger log = WardenLogging.getLog(NodeActionResources.class);


    @POST
    @Path("watch")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel watchNode(NodeSettingModel nodeSettingModel) {
        try {
            NodeSettingDTO nodeSettingDTO = nodeSettingDAO.getNodeByID(nodeSettingModel.getNodeID());
            //validator
            if (StringUtils.isBlank(nodeSettingDTO.getZkID())) {
                throw new RuntimeException("zookeeper should mapping first");
            }
            nodeDataStorageInstance.watchNodeData(nodeSettingDTO);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            log.error("watch failed", t);
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("unwatch")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel unWatchNode(NodeSettingModel nodeSettingModel) {
        try {
            String nodeID = nodeSettingModel.getNodeID();
            nodeDataStorageInstance.unwatchNodeData(nodeID);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            log.error("watch failed", t);
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("getData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public NodeDataModel getData(NodeSettingModel nodeSettingModel) throws Exception {
        Map<String, String> dataMapValue = nodeDataStorageInstance.getAllMapDataFromNode(nodeSettingModel.getNodeID());
        // get and parse default value
        NodeSettingDTO NodeSettingDTO = nodeSettingDAO.getNodeByID(nodeSettingModel.getNodeID());
        String extraInfoStr = NodeSettingDTO.getExtraInfo();
        JSONObject extraInfoJsonObj = null;
        try {
            if (StringUtils.isNotBlank(extraInfoStr)) {
                extraInfoJsonObj = new JSONObject(extraInfoStr);
            }
        } catch (Throwable t) {
            log.warn("parse json error", t);
        }

        Map<String, NodeDataValue> dataMap = new HashMap<String, NodeDataValue>();

        Iterator<Map.Entry<String, String>> it = dataMapValue.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            NodeDataValue nodeDataValue = new NodeDataValue();
            if (null != extraInfoJsonObj) {
                parseExtraInfo(extraInfoJsonObj, key, nodeDataValue);
            }
            nodeDataValue.setValue(value);
            dataMap.put(key, nodeDataValue);
        }

        NodeDataModel nodeDataModel = new NodeDataModel();
        nodeDataModel.setNodeID(nodeSettingModel.getNodeID());
        nodeDataModel.setDatamap(dataMap);
        return nodeDataModel;
    }

    /**
     * Json Schema
     * <p/>
     * {
     * "a":
     * {
     * "defaultValues":
     * [
     * "ATrue",
     * "AFalse"
     * ],
     * "description":"Atest"
     * }
     * }
     *
     * @param extraInfoJsonObj
     * @param key
     * @return
     */
    private void parseExtraInfo(JSONObject extraInfoJsonObj, String key, NodeDataValue nodeDataValue) {
        //put default choose value
        try {
            if (!extraInfoJsonObj.isNull(key)) {
                JSONObject keyExtraInfo = extraInfoJsonObj.getJSONObject(key);
                if (null != keyExtraInfo) {
                    // set defaultChooseValues
                    JSONArray defaultChooseArray = keyExtraInfo.getJSONArray("defaultValues");
                    if (null != defaultChooseArray) {
                        List<String> defaultChooseValueList = new ArrayList<String>();
                        for (int i = 0; i < defaultChooseArray.length(); i++) {
                            String defaultValue = defaultChooseArray.getString(i);
                            defaultChooseValueList.add(defaultValue);
                        }
                        nodeDataValue.setDefaultChooseValueList(defaultChooseValueList);
                    }
                }
                //  set description
                Object description = keyExtraInfo.get("description");
                if (null != description) {
                    nodeDataValue.setDescription(description.toString());
                }
            }
        } catch (Throwable t) {
            log.warn("get and parse json error", t);
        }
    }

    @POST
    @Path("changeData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel changeData(NodeMapDataChangedModel nodeMapDataChangedModel) {
        try {
            String nodeID = nodeMapDataChangedModel.getNodeID();
            String key = nodeMapDataChangedModel.getKey();
            String value = nodeMapDataChangedModel.getValue();
            if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
                return wrapThrowableToReturn(DefineMsg.FAILED_MSG + ", key or value is blank ,can not be updated", DefineMsg.FAILED, null);
            }
            log.info("change data from nodeID=" + nodeID + ",key=" + key + ",value=" + value);
            nodeDataStorageInstance.putOrModifyMapDataFromNode(nodeID, key, value);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            log.error("change data failed", t);
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }

    @POST
    @Path("deleteData")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ReturnMessageModel deleteData(NodeMapDataChangedModel nodeMapDataChangedModel) {
        try {
            String nodeID = nodeMapDataChangedModel.getNodeID();
            String key = nodeMapDataChangedModel.getKey();
            if (StringUtils.isBlank(key)) {
                return wrapThrowableToReturn(DefineMsg.FAILED_MSG + ",key is blank , can not be updated", DefineMsg.FAILED, null);
            }
            log.info("delete data from nodeID=" + nodeID + ",key=" + key);
            nodeDataStorageInstance.nodeDataDelete(nodeID, key);
            return new ReturnMessageModel(DefineMsg.SUCCESS_MSG, DefineMsg.OK);
        } catch (Throwable t) {
            log.error("change data failed", t);
            return wrapThrowableToReturn(DefineMsg.FAILED_MSG, DefineMsg.FAILED, t);
        }
    }
}
