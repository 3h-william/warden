package newegg.ec.disnotice.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DisNoticeSettingModel {
    private List<NodeSettingModel> nodeList;
    private String zkServers;

    public DisNoticeSettingModel(){};

    public DisNoticeSettingModel(List<NodeSettingModel> nodeList, String zkServers) {
        this.nodeList = nodeList;
        this.zkServers = zkServers;
    }

    public List<NodeSettingModel> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeSettingModel> nodeList) {
        this.nodeList = nodeList;
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }
}
