package newegg.ec.disnotice.tool.dynamicconf;

import newegg.ec.disnotice.core.NodeEvent;
import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.collections.DisNoticeMapChangedListener;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wz68 on 2015/8/1.
 * <p/>
 * R/W Type for client map(conf) from zk
 * consumer should implement validator data if data is not correct, that can avoid terrible crash in your apps.
 */
public class DynamicConfigurationFactory {
    private static Logger log = WardenLogging.getLog(DynamicConfigurationFactory.class);
    private ZKManager zkManager;
    private Map<String, String> confCacheMap = new ConcurrentHashMap<String, String>();
    private Map<String, DynamicConfValidator> confValidatorMap = new ConcurrentHashMap<String, DynamicConfValidator>();
    private DisNoticeMap<String> dnm;

    private static class DynamicConfigurationFactoryHandler {
        private static DynamicConfigurationFactory dynamicConfigurationFactory;

        static {
            dynamicConfigurationFactory = new DynamicConfigurationFactory();
        }
    }

    public static DynamicConfigurationFactory getDynamicConfigurationInstance() {
        return DynamicConfigurationFactoryHandler.dynamicConfigurationFactory;
    }

    public void init(ZKManager zkmgr, String nodePath) throws Exception {
        zkManager = zkmgr;
        // TODO can be set read only
        dnm = new DisNoticeMap<String>(zkmgr, nodePath, String.class, DisNoticeMap.DisNoticeMapModel.RW);
        DynamicChangedListener dynamicChangedListener = new DynamicChangedListener(this);
        dnm.addDataChangedListener(dynamicChangedListener);
    }

    public void initKeyValue(String key, String value) {
        if (null != value) {
            this.confCacheMap.put(key, value);
        }
    }

    /**
     * only register validator
     * if need sync data first , cau use @registerValidatorListenerAndSync
     *
     * @param key
     * @param dynamicConfValidator
     */
    public void registerValidatorListener(String key, DynamicConfValidator dynamicConfValidator) {
        this.confValidatorMap.put(key, dynamicConfValidator);
    }

    /**
     * register validator , and sync data from disNoticeMap
     *
     * @param key
     * @param dynamicConfValidator
     */
    public void registerValidatorListenerAndSync(String key, DynamicConfValidator dynamicConfValidator) {
        registerValidatorListener(key, dynamicConfValidator);
        String value = this.dnm.get(key);
        validatorAndUpdate(this, key, NodeEvent.Type.SYNC, value);
    }


    public String get(String key) {
        return confCacheMap.get(key);
    }

    protected void putKeyCache(String key, String value) {
        log.info("conf data changed,key=" + key + ",value=" + value);
        confCacheMap.put(key, value);
    }

    protected DynamicConfValidator getValidatorInstance(String key) {
        return confValidatorMap.get(key);
    }


    /**
     * check by validator instance and update cache data
     *
     * @param dynamicConfigurationFactory
     * @param ChangeKeyName
     * @param nodeEventType
     * @param value
     */
    private void validatorAndUpdate(DynamicConfigurationFactory dynamicConfigurationFactory,
                                    String ChangeKeyName, NodeEvent.Type nodeEventType, String value) {
        DynamicConfValidator dynamicConfValidator = dynamicConfigurationFactory.getValidatorInstance(ChangeKeyName);
        if (null != dynamicConfValidator && dynamicConfValidator.validator(value, nodeEventType)) {
            dynamicConfigurationFactory.putKeyCache(ChangeKeyName, value);
        }
        // if validator instance is null ,skip and set value directly
        if (null == dynamicConfValidator) {
            dynamicConfigurationFactory.putKeyCache(ChangeKeyName, value);
        }
    }

    class DynamicChangedListener implements DisNoticeMapChangedListener {
        private DynamicConfigurationFactory dynamicConfigurationFactory;

        public DynamicChangedListener(DynamicConfigurationFactory dynamicConfigurationFactory) {
            this.dynamicConfigurationFactory = dynamicConfigurationFactory;
        }

        @Override
        public void dataChangedEvent(DisNoticeMap disNoticeMap, String ChangeKeyName, NodeEvent.Type nodeEventType) {
            String value = (String) disNoticeMap.get(ChangeKeyName);
            log.debug("data changed event, ChangeKeyName = " + ChangeKeyName + ",nodeEventType=" + nodeEventType + ",value=" + value);
            // if validator pass ,then set value to cache
            validatorAndUpdate(dynamicConfigurationFactory, ChangeKeyName, nodeEventType, value);
        }
    }
}



