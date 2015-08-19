package newegg.ec.disnotice.rest.model;

import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GroupSettingModel {
    private String groupID;
    private String groupName;
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

    public Set<String> getNodes() {
        return nodes;
    }

    public void setNodes(Set<String> nodes) {
        this.nodes = nodes;
    }

    public GroupSettingModel() {
    }

    public GroupSettingModel(String groupID, String groupName, Set<String> nodes) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.nodes = nodes;
    }
}
