package newegg.ec.disnotice.core.conf;

import newegg.ec.disnotice.core.pathsystem.PathConstructType;

/**
 * how to create a InstanceConfigurationPath, please see wiki
 * <p/>
 * Created by wz68 on 2015/7/8.
 */
public class InstanceConfiguration {
    private PathConstructType.InstancePathType instancePathType;
    private PathConstructType.InstancePathScope instancePathScope;
    private String instanceName;

    /**
     * @param $instancePathType
     * @param $instancePathScope
     * @param $instanceName
     */
    public InstanceConfiguration(
            PathConstructType.InstancePathType $instancePathType,
            PathConstructType.InstancePathScope $instancePathScope,
            String $instanceName) {
        // validator
        if (null == $instancePathType || null == $instancePathScope || null == $instanceName) {
            throw new NullPointerException("instance configuration has null value for construct");
        }
        this.instancePathType = $instancePathType;
        this.instancePathScope = $instancePathScope;
        this.instanceName = $instanceName;
    }

    public PathConstructType.InstancePathType getInstancePathType() {
        return instancePathType;
    }

    public PathConstructType.InstancePathScope getInstancePathScope() {
        return instancePathScope;
    }

    public String getInstanceName() {
        return instanceName;
    }
}