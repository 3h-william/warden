package newegg.ec.disnotice.rest.model;

import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import newegg.ec.disnotice.business.dto.ZKSettingDTO;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ZKSettingAllModel {
    private List<ZKSettingModel> zkList;

    public ZKSettingAllModel() {
    }

    public ZKSettingAllModel(List<ZKSettingModel> zkList) {
        this.zkList = zkList;
    }

    public List<ZKSettingModel> getZkList() {
        return zkList;
    }

    public void setZkList(List<ZKSettingModel> zkList) {
        this.zkList = zkList;
    }
}
