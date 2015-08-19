package newegg.ec.disnotice.business.dao.base;

import newegg.ec.disnotice.business.dto.ZKSettingDTO;

import java.util.Set;

/**
 * Created by wz68 on 2015/7/17.
 */
public interface IZKSettingDAO extends IBaseDAO {
    public static String DB_TABLE_NAME = "ZKSetting";

    public void saveTable(ZKSettingDTO zkSettingDTO);

    public Set<ZKSettingDTO> getAllZK();

    public boolean existZKName(String zkName);

    public void deleteZKByID(String zkID) throws Exception;

    public ZKSettingDTO getZKByID(String zkID) throws  Exception;
}
