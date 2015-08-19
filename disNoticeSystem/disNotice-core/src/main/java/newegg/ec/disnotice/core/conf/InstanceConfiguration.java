package newegg.ec.disnotice.core.conf;

import sun.applet.AppletIOException;

/**
 * Created by wz68 on 2015/7/8.
 */
public class InstanceConfiguration {
    public String instanceName;
    public String instanceVersion;

    public InstanceConfiguration(String instanceName, String instanceVersion) {
        this.instanceName = instanceName;
        this.instanceVersion = instanceVersion;
    }
}