package newegg.ec.disnotice.core.zk;

import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.WardenLogging;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;

/**
 * Created by wz68 on 2015/7/10.
 * <p/>
 * ZK Manager with Curator
 */
public class ZKManagerAsCurator extends ZKManager {

    private static Logger log = WardenLogging.getLog(ZKManagerAsCurator.class);

    private static int retryInSleep = 1000;
    private static int retryTimes = 3;

    private CuratorFramework curatorClient;

    public ZKManagerAsCurator(PropertyLoader propLoader) {
        super(propLoader);
    }

    public ZKManagerAsCurator(String zkServers) {
        super(zkServers);
    }


    @Override
    public void init() {
        // TODO default retry policy
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(retryInSleep, retryTimes);
        curatorClient = CuratorFrameworkFactory.newClient(super.connectString, retryPolicy);
    }

    @Override
    public void createPath(String path, byte[] value, CreateMode createMode) throws Exception {
        curatorClient.create().creatingParentsIfNeeded().withMode(createMode).forPath(path, value);
    }

    @Override
    public boolean existPath(String path) throws Exception {
        return (curatorClient.checkExists().forPath(path) != null);
    }

    @Override
    public void setData(String path, byte[] value) throws Exception {
        curatorClient.setData().forPath(path, value);
    }

    @Override
    public Object getZkClient() throws Exception {
        return curatorClient;
    }

    @Override
    public void close() throws Exception {
        if(null!=curatorClient){
            curatorClient.close();
        }
    }

    @Override
    public void deleteIfExist(String path) {
        try {
            if (existPath(path))
                curatorClient.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            log.warn("node delete failed , " + path + ",", e);
        }
    }

    @Override
    public void startZKConnection() {
        curatorClient.start();
    }

    /**
     *
     * if zk is not start success , then will throws exception
     *
     * @throws Exception
     */
    @Override
    public void checkZKStartSuccess() throws Exception {
        curatorClient.getZookeeperClient().getZooKeeper();
    }

    @Override
    public void startNodeListener(final String nodeName, final NodeListenerCallback nodeListenerCallback) throws Exception {
        final NodeCache nodeCache = new NodeCache(curatorClient, nodeName);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData currentData = nodeCache.getCurrentData();
                if (null != currentData) {
                    nodeListenerCallback.valueChanged(new String(currentData.getData()));
                } else {
                    log.warn("changed value null, nodeName = " + nodeName);
                }
            }
        });
        nodeCache.start();
    }

    @Override
    public Object startTreeListener(String nodeName, final TreeListenerCallback treeListenerCallback) throws Exception {
        final TreeCache treeCache = new TreeCache(curatorClient, nodeName);
        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case NODE_ADDED: {
                        treeListenerCallback.node_add(event.getData().getPath(), event.getData().getData());
                        break;
                    }
                    case NODE_UPDATED: {
                        treeListenerCallback.node_update(event.getData().getPath(), event.getData().getData());
                        break;
                    }
                    case NODE_REMOVED: {
                        treeListenerCallback.node_removed(event.getData().getPath());
                        break;
                    }
                    case INITIALIZED: {
                        treeListenerCallback.node_initialized();
                        break;
                    }
                    default:
                        treeListenerCallback.other_event(event.getType().name());
                }
            }
        };
        treeCache.getListenable().addListener(listener);
        treeCache.start();
        return treeCache;
    }

    @Override
    public void stopTreeListener(Object object) throws Exception {
        TreeCache tc = (TreeCache) object;
        tc.close();;
    }


}
