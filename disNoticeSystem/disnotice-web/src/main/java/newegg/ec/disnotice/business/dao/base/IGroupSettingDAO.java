package newegg.ec.disnotice.business.dao.base;

import newegg.ec.disnotice.business.dto.GroupSettingDTO;

import java.util.Set;

/**
 * Created by wz68 on 2015/7/17.
 */
public interface IGroupSettingDAO extends IBaseDAO {
    public static String DB_TABLE_NAME = "GroupSetting";

    public void saveTable(GroupSettingDTO groupSetting);

    public GroupSettingDTO getGroupByID(String groupID);

    public Set<GroupSettingDTO> getAllByGroupName(String groupName);

    public Set<GroupSettingDTO> getAllGroup();

    public boolean existGroupName(String groupName);

    public void deleteGroupByID(String groupID) throws Exception;

}
