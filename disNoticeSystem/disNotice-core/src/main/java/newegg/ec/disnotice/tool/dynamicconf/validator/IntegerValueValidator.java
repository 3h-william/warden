package newegg.ec.disnotice.tool.dynamicconf.validator;

import newegg.ec.disnotice.core.NodeEvent;
import newegg.ec.disnotice.tool.dynamicconf.DynamicConfValidator;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

/**
 * Created by wz68 on 2015/8/1.
 */
public class IntegerValueValidator implements DynamicConfValidator {
    private static Logger log = WardenLogging.getLog(IntegerValueValidator.class);
    @Override
    public boolean validator(Object value, NodeEvent.Type nodeEventType) {
        try {
            int x = Integer.parseInt((String) value);
            return true;
        } catch (Throwable e) {
            log.warn("validator int false,value="+value,e);
            return false;
        }
    }
}
