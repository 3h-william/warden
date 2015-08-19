package newegg.ec.disnotice.business.dto;


import newegg.ec.disnotice.business.dao.base.INodeSettingDAO;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = INodeSettingDAO.DB_TABLE_NAME)
public class NodeSettingDTO extends BaseDTO implements Serializable, Comparable<NodeSettingDTO> {

    @Id
    private String nodeID;
    private String nodeName;
    private String nodePath;
    private String nodeType;
    private String zkID;
    private String extraInfo;

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

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public int compareTo(NodeSettingDTO o) {
        if (null == o)
            return 0;
        return this.getNodeName().compareTo(o.getNodeName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeSettingDTO that = (NodeSettingDTO) o;

        if (nodeID != null ? !nodeID.equals(that.nodeID) : that.nodeID != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nodeID != null ? nodeID.hashCode() : 0;
    }
}