package business.sqlite;

import newegg.ec.disnotice.business.dao.DAOInstance;
import newegg.ec.disnotice.business.dao.base.IGroupSettingDAO;
import newegg.ec.disnotice.business.dao.util.IdWorker;
import newegg.ec.disnotice.business.dto.GroupSettingDTO;

/**
 * Created by wz68 on 2015/7/17.
 */
public class TestGroupSetting {

    public static void main(String[] args) {

        GroupSettingDTO groupSettingDTO1 = new GroupSettingDTO();
        groupSettingDTO1.setGroupID(IdWorker.getGroupID());
        groupSettingDTO1.setGroupName("testGroup1");


        GroupSettingDTO groupSettingDTO2 = new GroupSettingDTO();
        groupSettingDTO2.setGroupID(IdWorker.getGroupID());
        groupSettingDTO2.setGroupName("testGroup2");

        IGroupSettingDAO groupSettingDAO = DAOInstance.getGroupSettingDAO();
        groupSettingDAO.saveTable(groupSettingDTO1);
        groupSettingDAO.saveTable(groupSettingDTO2);

        for (GroupSettingDTO groupSettingDTO : groupSettingDAO.getAllGroup()) {
            System.out.println(groupSettingDTO.getGroupName());
        }
    }
}
