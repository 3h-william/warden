package zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz68 on 2015/7/10.
 */
public class ZkTest {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException, NoSuchAlgorithmException {

       final ZooKeeper zk = new ZooKeeper("echadoop03:2181", 200000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("get new event : " + event);
            }
        });



        List<ACL> acls = new ArrayList<ACL>();
      //  Id id1 = new Id("disNotice", DigestAuthenticationProvider.generateDigest("admin:admin"));
        Id id1 = new Id("world", "anyone");
        ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);
        acls.add(acl1);
       // zk.create("/t","test".getBytes(),acls, CreateMode.PERSISTENT);

        zk.exists("/mytest",true);

        while(true){
            Thread.sleep(100000);
        }

    }
}
