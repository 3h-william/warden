package newegg.ec.disnotice.rest.model;

import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GroupSettingAllModel {
    private List<GroupSettingModel> groupList;

    public GroupSettingAllModel() {
    }

    public GroupSettingAllModel(List<GroupSettingModel> groupList) {
        this.groupList = groupList;
    }

    public List<GroupSettingModel> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupSettingModel> groupList) {
        this.groupList = groupList;
    }
}
