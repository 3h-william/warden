package com.newegg.ec.warden.watch;

import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

/**
 * Created by wz68 on 2015/7/9.
 */
public abstract class BaseWatch implements WatchConstants {
    private static Logger log = WardenLogging.getLog(BaseWatch.class);
    protected PropertyLoader propLoader;
    protected LogTimeReporter logTimeReporter;

    public BaseWatch(PropertyLoader propLoader) {
        this.propLoader = propLoader;
        try {
            initLogTimeReporter(propLoader);
        } catch (Exception e) {
            log.error("init watch monitor failed,shoulde exit", e);
            System.exit(-1);
        }
    }

    public void initLogTimeReporter(PropertyLoader propLoader) throws Exception {
        String zkServers = this.propLoader.getValue(DISNOTICE_ZK_CONNECT_SERVERS);
        this.logTimeReporter = new LogTimeReporter(propLoader.getValue(WATCH_INSTANCE_NAME), zkServers);
    }

    public abstract void watch();
}
