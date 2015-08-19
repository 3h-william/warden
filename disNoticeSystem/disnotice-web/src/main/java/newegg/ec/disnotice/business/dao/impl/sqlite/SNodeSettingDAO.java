package newegg.ec.disnotice.business.dao.impl.sqlite;

import com.google.common.collect.Sets;
import newegg.ec.disnotice.business.dao.base.INodeSettingDAO;
import newegg.ec.disnotice.business.dto.NodeSettingDTO;
import org.apache.commons.lang3.StringUtils;

import javax.xml.soap.Node;
import java.util.*;

/**
 * Created by wz68 on 2015/7/17.
 */
public class SNodeSettingDAO extends SBaseDAO<NodeSettingDTO> implements INodeSettingDAO {
    private static final String COLUMN_NODE_NAME = "nodeName";
    private static final String COLUMN_ZK_ID = "zkID";

    @Override
    protected void parseToTake(NodeSettingDTO obj) {
    }

    @Override
    protected void parseToSave(NodeSettingDTO obj) {
    }

    @Override
    public void saveTable(NodeSettingDTO nodeSettingDTO) {
        saveTableAsDTO(nodeSettingDTO);
    }

    @Override
    public Set<NodeSettingDTO> getAllNode() {
        return getAll(NodeSettingDTO.class);
    }

    @Override
    public boolean existNodeName(String nodeName) {
        return existColumnValue(COLUMN_NODE_NAME, nodeName, NodeSettingDTO.class);
    }

    @Override
    public void deleteNodeByID(String nodeID) throws Exception {
        delTableByKey(NodeSettingDTO.class, nodeID);
    }

    @Override
    public NodeSettingDTO getNodeByID(String nodeID) throws Exception {
        return getById(nodeID, NodeSettingDTO.class);
    }

    @Override
    public Set<NodeSettingDTO> getNodeByIDs(Collection<String> nodeIDs) throws Exception {
        return getByIds(nodeIDs.toArray(new String[nodeIDs.size()]), NodeSettingDTO.class);
    }

    @Override
    public Set<NodeSettingDTO> getNodeByZKID(String zkID) throws Exception {
        return getByColumnValue(COLUMN_ZK_ID, zkID, NodeSettingDTO.class);
    }
}
