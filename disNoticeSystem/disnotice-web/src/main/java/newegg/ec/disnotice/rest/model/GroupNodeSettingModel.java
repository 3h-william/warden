package newegg.ec.disnotice.rest.model;

import java.util.List;

/**
 * Created by wz68 on 2015/7/28.
 */
public class GroupNodeSettingModel {
    private String groupID;
    private List<NodeSettingModel> selectedNodeList;
    private List<NodeSettingModel> unSelectedNodeList;

    public GroupNodeSettingModel() {
    }

    public GroupNodeSettingModel(String groupID, List<NodeSettingModel> selectedNodeList, List<NodeSettingModel> unSelectedNodeList) {
        this.groupID = groupID;
        this.selectedNodeList = selectedNodeList;
        this.unSelectedNodeList = unSelectedNodeList;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public List<NodeSettingModel> getSelectedNodeList() {
        return selectedNodeList;
    }

    public void setSelectedNodeList(List<NodeSettingModel> selectedNodeList) {
        this.selectedNodeList = selectedNodeList;
    }

    public List<NodeSettingModel> getUnSelectedNodeList() {
        return unSelectedNodeList;
    }

    public void setUnSelectedNodeList(List<NodeSettingModel> unSelectedNodeList) {
        this.unSelectedNodeList = unSelectedNodeList;
    }
}
