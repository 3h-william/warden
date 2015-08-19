package business;

import newegg.ec.disnotice.business.dao.util.IdWorker;

/**
 * Created by wz68 on 2015/7/17.
 */
public class IDWorkerTest {
    public static void main(String[] args) {
        System.out.println(IdWorker.getGroupID());
        String nodeID="1";
        String key ="2";
        String value ="3";
        String x =    "{ nodeID:"+nodeID+",datamap:{"+key+":"+value+"}}" ;
    }
}
