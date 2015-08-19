package newegg.ec.disnotice.rest.model;

import newegg.ec.disnotice.business.dto.ZKSettingDTO;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class NodeSettingAllModel {
    private List<NodeSettingModel> nodeList;

    public NodeSettingAllModel() {
    }

    public NodeSettingAllModel(List<NodeSettingModel> nodeList) {
        this.nodeList = nodeList;
    }

    public List<NodeSettingModel> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeSettingModel> nodeList) {
        this.nodeList = nodeList;
    }
}
