package newegg.ec.disnotice.business.dao;

import newegg.ec.disnotice.business.dao.base.IGroupSettingDAO;
import newegg.ec.disnotice.business.dao.base.INodeSettingDAO;
import newegg.ec.disnotice.business.dao.base.IZKSettingDAO;
import newegg.ec.disnotice.business.dao.impl.sqlite.SGroupSettingDAO;
import newegg.ec.disnotice.business.dao.impl.sqlite.SNodeSettingDAO;
import newegg.ec.disnotice.business.dao.impl.sqlite.SZKSettingDAO;

/**
 * Created by wz68 on 2015/7/17.
 */
public class DAOInstance {

    private static class GroupSettingDAOHandler {
        private static IGroupSettingDAO groupSettingDAO;

        static {
            groupSettingDAO = new SGroupSettingDAO();
        }
    }

    private static class ZKSettingDAOHandler {
        private static IZKSettingDAO izkSettingDAO;

        static {
            izkSettingDAO = new SZKSettingDAO();
        }
    }

    private static class NodeSettingDAOHandler {
        private static INodeSettingDAO iNodeSettingDAO;

        static {
            iNodeSettingDAO = new SNodeSettingDAO();
        }
    }


    public static IGroupSettingDAO getGroupSettingDAO() {
        return GroupSettingDAOHandler.groupSettingDAO;
    }

    public static IZKSettingDAO getZKSettingDAO() {
        return ZKSettingDAOHandler.izkSettingDAO;
    }

    public static INodeSettingDAO getNodeSettingDAO() {
        return NodeSettingDAOHandler.iNodeSettingDAO;
    }

}
