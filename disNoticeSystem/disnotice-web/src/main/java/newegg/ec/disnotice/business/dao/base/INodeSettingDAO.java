package newegg.ec.disnotice.business.dao.base;

import newegg.ec.disnotice.business.dto.GroupSettingDTO;
import newegg.ec.disnotice.business.dto.NodeSettingDTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by wz68 on 2015/7/17.
 */
public interface INodeSettingDAO extends IBaseDAO {
    public static String DB_TABLE_NAME = "NodeSetting";

    public void saveTable(NodeSettingDTO nodeSettingDTO);

    public Set<NodeSettingDTO> getAllNode();

    public boolean existNodeName(String nodeName);

    public void deleteNodeByID(String nodeID) throws Exception;

    public NodeSettingDTO getNodeByID(String nodeID) throws  Exception;

    public Set<NodeSettingDTO> getNodeByIDs(Collection<String> nodeIDs) throws  Exception;

    public Set<NodeSettingDTO> getNodeByZKID(String zkID) throws  Exception;


}
