package zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;

/**
 * Created by wz68 on 2015/7/10.
 */
public class CuratorTest {

    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("echadoop08:2181", retryPolicy);
        client.start();


//        final NodeCache nodeCache = new NodeCache(client, "/mytest");
//        nodeCache.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                ChildData currentData = nodeCache.getCurrentData();
//                System.out.println("data change watched, and current data = " + new String(currentData.getData()));
//            }
//        });
//        nodeCache.start();

//        final PathChildrenCache pathCached = new PathChildrenCache(client, "/mytest", true);
//        pathCached.getListenable().addListener(new PathChildrenCacheListener() {
//            @Override
//            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
//                PathChildrenCacheEvent.Type eventType = event.getType();
//                switch (eventType) {
//                    case CONNECTION_RECONNECTED:
//                        pathCached.rebuild();
//                        break;
//                    case CONNECTION_SUSPENDED:
//                    case CONNECTION_LOST:
//                        System.out.println("Connection error,waiting...");
//                        break;
//                    default:
//                        System.out.println("PathChildrenCache changed : {path:" + event.getData().getPath() + " data:" +
//                                new String(event.getData().getData()) + "}");
//                }
//            }
//        });
//        pathCached.start();

        TreeCache treeCache = new TreeCache(client,"/mytest");

        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case NODE_ADDED: {
                        System.out.println("TreeNode added: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                                + new String(event.getData().getData()));
                        break;
                    }
                    case NODE_UPDATED: {
                        System.out.println("TreeNode changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()) + ", value: "
                                + new String(event.getData().getData()));
                        break;
                    }
                    case NODE_REMOVED: {
                        System.out.println("TreeNode removed: " + event.getData().getPath());
                        break;
                    }
                    default:
                        System.out.println("Other event: " + event.getType().name());
                }
            }
        };
        treeCache.getListenable().addListener(listener);
        treeCache.start();

        Thread.sleep(100000);
    }


}
