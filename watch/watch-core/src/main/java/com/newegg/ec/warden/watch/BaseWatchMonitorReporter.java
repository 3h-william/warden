package com.newegg.ec.warden.watch;

import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.pathsystem.PathConstructType;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.tool.dynamicconf.DynamicConfigurationFactory;
import newegg.ec.disnotice.tool.dynamicconf.validator.IntegerValueValidator;
import newegg.ec.disnotice.tool.zk.ZKConnections;
import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * provide latency status function
 * <p/>
 * Created by wz68 on 2015/8/19.
 */
public abstract class BaseWatchMonitorReporter implements WatchConstants {
    private static Logger log = WardenLogging.getLog(BaseWatchMonitorReporter.class);
    protected PropertyLoader propertyLoader;
    protected DynamicConfigurationFactory dynamicConfigurationFactory;
    protected DisNoticeMap<String> dnm = null;
    // TODO concurrency control is not good in this condition
    public Map<String, Long> statusChangedLatency = new ConcurrentHashMap<String, Long>();

    public BaseWatchMonitorReporter(PropertyLoader propertyLoader) throws Exception {
        this.propertyLoader = propertyLoader;
        this.dynamicConfigurationFactory = DynamicConfigurationFactory.getDynamicConfigurationInstance();
        initDynamicConf();
        init();
    }

    private void init() throws Exception {
        // init
        InstanceConfiguration inc = new InstanceConfiguration(
                PathConstructType.InstancePathType.status,
                PathConstructType.InstancePathScope.cluster,
                propertyLoader.getValue(WATCH_REPORT_NAME));
        String mapRootPath = DisPathManager.getInstancePath(inc);
        log.info("init base watch monitor report zk path = " + mapRootPath);
        String zkServers = propertyLoader.getValue(DISNOTICE_ZK_CONNECT_SERVERS);
        ZKManager zkmgr = ZKConnections.getZKConnectionsInstance().get(zkServers);
        // init report map
        this.dnm = new DisNoticeMap<String>(zkmgr, mapRootPath, String.class, DisNoticeMap.DisNoticeMapModel.RW);
    }

    private void initDynamicConf() {
        // status.changed.latency.seconds
        Integer defaultStatusChangedLatencySeconds = Integer.parseInt(propertyLoader.getValue(STATUS_CHANGED_LATENCY_SECONDS, "30"));
        dynamicConfigurationFactory.initKeyValueWithCreateIfNotExist(STATUS_CHANGED_LATENCY_SECONDS, defaultStatusChangedLatencySeconds + "");
        dynamicConfigurationFactory.registerValidatorListenerAndSync(STATUS_CHANGED_LATENCY_SECONDS, new IntegerValueValidator());
    }

    /**
     * if some value changed should be in some latency ,that it can use this function
     *
     * @param key
     * @param value
     * @return
     */
    protected void latencyForValueChanged(String key, String value) {
        Long preRecord = statusChangedLatency.get(key);
        if (null == preRecord) {
            preRecord = System.currentTimeMillis();
            statusChangedLatency.put(key, preRecord);
            log.info("record " + key + "  , modify time for changed in latency");
        } else {
            Long latencyTime = Long.parseLong(dynamicConfigurationFactory.get(STATUS_CHANGED_LATENCY_SECONDS)) * 1000;
            Long nowTime = System.currentTimeMillis();
            // reach latency ,then change value & recover latency record
            if (latencyTime + preRecord < nowTime) {
                log.info(key + ", value changed , now value =" + value);
                this.dnm.put(key, value);
                recoverLatencyRecord(key);
            } else {
                log.info(key + ", waiting for changed in latency");
            }
        }
    }

    protected void recoverLatencyRecord(String key) {
        log.info(key + " recover latency record");
        statusChangedLatency.remove(key);
    }
}
