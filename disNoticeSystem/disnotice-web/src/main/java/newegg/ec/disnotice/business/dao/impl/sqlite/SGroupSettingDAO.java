package newegg.ec.disnotice.business.dao.impl.sqlite;

import com.google.common.collect.Sets;
import newegg.ec.disnotice.business.dao.base.IGroupSettingDAO;
import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by wz68 on 2015/7/17.
 */
public class SGroupSettingDAO extends SBaseDAO<GroupSettingDTO> implements IGroupSettingDAO {

    public static final String COLUMN_GROUP_NAME = "groupName";

    @Override
    protected void parseToTake(GroupSettingDTO obj) {
        String nodeStr = obj.getNodeStr();
        Set<String> nodeList = new TreeSet<String>();
        if (StringUtils.isNotEmpty(nodeStr)) {
            nodeList = Sets.newTreeSet(Arrays.asList(nodeStr.split(list_split_character)));
        }
        obj.setNodes(nodeList);
    }

    @Override
    protected void parseToSave(GroupSettingDTO obj) {
        Set<String> nodeList = obj.getNodes();
        if (null != nodeList && nodeList.size() != 0) {
            obj.setNodeStr(StringUtils.join(nodeList, list_split_character));
        } else {
            obj.setNodeStr("");
        }
    }


    @Override
    public void saveTable(GroupSettingDTO groupSetting) {
        saveTableAsDTO(groupSetting);
    }

    @Override
    public GroupSettingDTO getGroupByID(String groupID) {
        return getById(groupID,GroupSettingDTO.class);
    }

    @Override
    public Set<GroupSettingDTO> getAllByGroupName(String groupName) {
        return getByColumnValue(COLUMN_GROUP_NAME, groupName, GroupSettingDTO.class);
    }

    @Override
    public Set<GroupSettingDTO> getAllGroup() {
        return getAll(GroupSettingDTO.class);
    }

    @Override
    public boolean existGroupName(String groupName) {
        return existColumnValue(COLUMN_GROUP_NAME, groupName, GroupSettingDTO.class);
    }

    @Override
    public void deleteGroupByID(String groupID) throws Exception {
        delTableByKey(GroupSettingDTO.class, groupID);
    }
}
