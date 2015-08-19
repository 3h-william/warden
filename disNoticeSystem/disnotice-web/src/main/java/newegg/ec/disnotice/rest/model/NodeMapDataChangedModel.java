package newegg.ec.disnotice.rest.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by wz68 on 2015/7/16.
 */
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NodeMapDataChangedModel {
    private String nodeID;
    private String key;
    private String value;

    public NodeMapDataChangedModel() {
    }

    public NodeMapDataChangedModel(String nodeID, String key, String value) {
        this.nodeID = nodeID;
        this.key = key;
        this.value = value;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}


