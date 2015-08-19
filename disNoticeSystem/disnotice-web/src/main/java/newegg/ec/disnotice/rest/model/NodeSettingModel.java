package newegg.ec.disnotice.rest.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by wz68 on 2015/7/16.
 */
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NodeSettingModel {
    private String nodeID;
    private String nodeName;
    private String nodePath;
    private String nodeType;
    private String zkID;
    private boolean isWatch;
    private String extraInfoStr;

    public NodeSettingModel() {
    }

    public NodeSettingModel(String nodeID, String nodeName, String nodePath, String nodeType, String zkID, boolean isWatch, String extraInfoStr) {
        this.nodeID = nodeID;
        this.nodeName = nodeName;
        this.nodePath = nodePath;
        this.nodeType = nodeType;
        this.zkID = zkID;
        this.isWatch = isWatch;
        this.extraInfoStr = extraInfoStr;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getZkID() {
        return zkID;
    }

    public void setZkID(String zkID) {
        this.zkID = zkID;
    }

    public boolean isWatch() {
        return isWatch;
    }

    public void setWatch(boolean isWatch) {
        this.isWatch = isWatch;
    }

    public String getExtraInfoStr() {
        return extraInfoStr;
    }

    public void setExtraInfoStr(String extraInfoStr) {
        this.extraInfoStr = extraInfoStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeSettingModel that = (NodeSettingModel) o;

        if (nodeID != null ? !nodeID.equals(that.nodeID) : that.nodeID != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nodeID != null ? nodeID.hashCode() : 0;
    }

}
