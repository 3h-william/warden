package newegg.ec.disnotice.business.dto;


import newegg.ec.disnotice.business.dao.base.IGroupSettingDAO;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = IGroupSettingDAO.DB_TABLE_NAME)
public class GroupSettingDTO extends BaseDTO implements Serializable, Comparable<GroupSettingDTO> {

    @Id
    private String groupID;
    private String groupName;
    private String nodeStr;

    @Transient
    private Set<String> nodes;


    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNodeStr() {
        return nodeStr;
    }

    public void setNodeStr(String nodeStr) {
        this.nodeStr = nodeStr;
    }

    public Set<String> getNodes() {
        return nodes;
    }

    public void setNodes(Set<String> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int compareTo(GroupSettingDTO o) {
        if (null == o)
            return 0;
        return this.getGroupName().compareTo(o.getGroupName());
    }

}