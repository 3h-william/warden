package newegg.ec.disnotice.rest.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wz68 on 2015/7/16.
 */
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NodeDataModel {
    private Map<String, NodeDataValue> datamap;
    private String nodeID;

    public NodeDataModel() {
    }

    public NodeDataModel(Map<String, NodeDataValue> datamap, String nodeID) {
        this.datamap = datamap;
        this.nodeID = nodeID;
    }

    public Map<String, NodeDataValue> getDatamap() {
        return datamap;
    }

    public void setDatamap(Map<String, NodeDataValue> datamap) {
        this.datamap = datamap;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }
}


