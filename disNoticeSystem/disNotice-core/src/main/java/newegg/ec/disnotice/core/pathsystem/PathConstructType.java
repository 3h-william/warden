package newegg.ec.disnotice.core.pathsystem;

/**
 * Created by wz68 on 2015/8/21.
 */
public class PathConstructType {

    public enum InstancePathType {
        status, conf, heartbeat
    }

    public enum InstancePathScope {
        app, cluster
    }
}
