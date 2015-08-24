package com.newegg.ec.warden.watch;

import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.pathsystem.PathConstructType;
import newegg.ec.disnotice.tool.zk.ZKConnections;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

/**
 * Created by wz68 on 2015/8/18.
 */
public class LogTimeReporter implements WatchConstants {
    public final static String watchLastTimeFieldName = "last.update.time";
    private DisNoticeMap<String> dnm = null;
    private static FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    public LogTimeReporter(String instanceName, String zkServers) throws Exception {
        // init
        InstanceConfiguration inc = new InstanceConfiguration(
                PathConstructType.InstancePathType.heartbeat,
                PathConstructType.InstancePathScope.app,
                instanceName);
        String nodeMapRootPath = DisPathManager.getInstancePath(inc);
        this.dnm = new DisNoticeMap<String>(ZKConnections.getZKConnectionsInstance().get(zkServers),
                nodeMapRootPath, String.class, DisNoticeMap.DisNoticeMapModel.RW);
    }

    public void logTime() {
        Date date = new Date();
        String nowTimeStamp = sdf.format(date);
        dnm.put(watchLastTimeFieldName, nowTimeStamp);
    }

}
