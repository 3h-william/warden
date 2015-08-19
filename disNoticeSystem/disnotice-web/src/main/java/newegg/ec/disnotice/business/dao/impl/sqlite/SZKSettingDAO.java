package newegg.ec.disnotice.business.dao.impl.sqlite;

import newegg.ec.disnotice.business.dao.base.IZKSettingDAO;
import newegg.ec.disnotice.business.dto.ZKSettingDTO;

import java.util.Set;

/**
 * Created by wz68 on 2015/7/17.
 */
public class SZKSettingDAO extends SBaseDAO<ZKSettingDTO> implements IZKSettingDAO {

    public static final String COLUMN_ZK_NAME = "zkName";

    @Override
    protected void parseToTake(ZKSettingDTO obj) {

    }

    @Override
    protected void parseToSave(ZKSettingDTO obj) {

    }


    @Override
    public void saveTable(ZKSettingDTO zkSettingDTO) {
        saveTableAsDTO(zkSettingDTO);
    }


    @Override
    public Set<ZKSettingDTO> getAllZK() {
        return getAll(ZKSettingDTO.class);
    }

    @Override
    public boolean existZKName(String zkName) {
        return existColumnValue(COLUMN_ZK_NAME, zkName, ZKSettingDTO.class);
    }

    @Override
    public void deleteZKByID(String zkID) throws Exception {
        delTableByKey(ZKSettingDTO.class,zkID);
    }

    @Override
    public ZKSettingDTO getZKByID(String zkID) throws Exception {
        return getById(zkID,ZKSettingDTO.class);
    }

}
