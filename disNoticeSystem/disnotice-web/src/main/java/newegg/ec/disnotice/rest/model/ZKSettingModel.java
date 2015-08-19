package newegg.ec.disnotice.rest.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ZKSettingModel {
    private String zkID;
    private String zkServers;
    private String zkName;
    private boolean isConnect;


    public ZKSettingModel() {
    }

    public ZKSettingModel(String zkID, String zkServers, String zkName, boolean isConnect) {
        this.zkID = zkID;
        this.zkServers = zkServers;
        this.zkName = zkName;
        this.isConnect = isConnect;
    }

    public String getZkID() {
        return zkID;
    }

    public void setZkID(String zkID) {
        this.zkID = zkID;
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public String getZkName() {
        return zkName;
    }

    public void setZkName(String zkName) {
        this.zkName = zkName;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }
}
