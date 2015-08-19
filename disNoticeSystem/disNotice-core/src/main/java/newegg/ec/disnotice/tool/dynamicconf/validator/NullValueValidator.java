package newegg.ec.disnotice.tool.dynamicconf.validator;

import newegg.ec.disnotice.core.NodeEvent;
import newegg.ec.disnotice.tool.dynamicconf.DynamicConfValidator;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

/**
 * Simple impl
 */
public class NullValueValidator implements DynamicConfValidator {
    private static Logger log = WardenLogging.getLog(NullValueValidator.class);

    @Override
    public boolean validator(Object value, NodeEvent.Type nodeEventType) {
        try {
            String str = (String) value;
            if (null == value || ((String) value).equalsIgnoreCase("null")) {
                return false;
            }
            return true;
        } catch (Throwable e) {
            log.warn("validator error,",e);
            return false;
        }
    }

}