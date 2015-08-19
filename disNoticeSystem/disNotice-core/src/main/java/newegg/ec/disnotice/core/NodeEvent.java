package newegg.ec.disnotice.core;

/**
 * Created by wz68 on 2015/8/1.
 */
public class NodeEvent {
    /**
     * ADD , UPDATE, REMOVE is event by zk
     * SYNC is event by dynamic conf
     */
    public enum Type {
        SYNC, ADD, UPDATE, REMOVE
    }
}
