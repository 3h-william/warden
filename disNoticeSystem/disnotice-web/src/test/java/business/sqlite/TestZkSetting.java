package business.sqlite;

import newegg.ec.disnotice.business.dao.DAOInstance;
import newegg.ec.disnotice.business.dao.base.IGroupSettingDAO;
import newegg.ec.disnotice.business.dao.base.IZKSettingDAO;
import newegg.ec.disnotice.business.dao.util.IdWorker;
import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import newegg.ec.disnotice.business.dto.ZKSettingDTO;

/**
 * Created by wz68 on 2015/7/17.
 */
public class TestZkSetting {

    public static void main(String[] args) {

        ZKSettingDTO zkSettingDTO = new ZKSettingDTO();
        zkSettingDTO.setZkID(IdWorker.getZKID());
        zkSettingDTO.setZkName("test zk name");
        zkSettingDTO.setZkServers("echadoop08:2181");



        IZKSettingDAO zkSettingDAO = DAOInstance.getZKSettingDAO();
        zkSettingDAO.saveTable(zkSettingDTO);

    }
}
