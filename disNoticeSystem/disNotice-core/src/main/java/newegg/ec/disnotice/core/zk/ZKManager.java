package newegg.ec.disnotice.core.zk;

import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.WardenLogging;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;

/**
 * Created by wz68 on 2015/7/10.
 * <p/>
 * abstract ZK Manager, can use curator or others client implements
 */
public abstract class ZKManager {
    private final String ZK_CONNECT_SERVERS = "disnotice.zk.connect.servers";
    private static Logger log = WardenLogging.getLog(ZKManager.class);
    private PropertyLoader propLoader;
    protected String connectString;

    public ZKManager(PropertyLoader propLoader) {
        iniZKmanager(propLoader);
    }

    private void iniZKmanager(PropertyLoader propLoader) {
        this.propLoader = propLoader;
        String servers = propLoader.getValue(ZK_CONNECT_SERVERS);
        if (null == servers) {
            log.error("null value of zk servers, exit");
        }
        this.connectString = servers;
        //
        init();
    }

    public ZKManager(String connectServers) {
        propLoader = new PropertyLoader(null);
        propLoader.getProp().setProperty(ZK_CONNECT_SERVERS, connectServers);
        iniZKmanager(propLoader);
    }

    public abstract void init();

    public abstract void deleteIfExist(String path);

    public abstract void startZKConnection();

    public abstract void checkZKStartSuccess() throws Exception;

    public abstract void startNodeListener(String nodeName, NodeListenerCallback nodeListenerCallback) throws Exception;

    public abstract Object startTreeListener(String nodeName, TreeListenerCallback treeListenerCallback) throws Exception;

    public abstract void stopTreeListener(Object object) throws Exception;

    public abstract void createPath(String path, byte[] value, CreateMode createMode) throws Exception;

    public abstract boolean existPath(String path) throws Exception;

    public abstract void setData(String path, byte[] value) throws Exception;

    public abstract Object getZkClient() throws Exception;

    public abstract void close() throws  Exception;

    public void updatePath(String path, byte[] value) throws Exception {
        if (existPath(path)) {
            setData(path, value);
        } else {
            createPath(path, value, CreateMode.PERSISTENT);
        }
    }

    public void initAndStart() throws Exception {
        init();
        startZKConnection();
        checkZKStartSuccess();
    }


    public interface NodeListenerCallback {
        public void valueChanged(String value);
    }

    public interface TreeListenerCallback {
        public void node_add(String path, byte[] value) throws Exception;

        public void node_update(String path, byte[] value) throws Exception;

        public void node_removed(String path) throws Exception;

        public void node_initialized() throws Exception;

        public void other_event(String eventName) throws Exception;
    }

}



