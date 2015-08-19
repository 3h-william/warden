package newegg.ec.disnotice.business.dto;


import newegg.ec.disnotice.business.dao.base.IZKSettingDAO;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = IZKSettingDAO.DB_TABLE_NAME)
public class ZKSettingDTO extends BaseDTO implements Serializable, Comparable<ZKSettingDTO> {
    @Id
    private String zkID;
    private String zkName;
    private String zkServers;


    public String getZkID() {
        return zkID;
    }

    public void setZkID(String zkID) {
        this.zkID = zkID;
    }

    public String getZkName() {
        return zkName;
    }

    public void setZkName(String zkName) {
        this.zkName = zkName;
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    @Override
    public int compareTo(ZKSettingDTO o) {
        if (null == o)
            return 0;
        return this.getZkName().compareTo(o.getZkName());
    }


}