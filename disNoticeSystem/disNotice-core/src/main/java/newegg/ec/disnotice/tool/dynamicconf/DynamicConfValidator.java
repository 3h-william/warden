package newegg.ec.disnotice.tool.dynamicconf;

import newegg.ec.disnotice.core.NodeEvent;

/**
 * Created by wz68 on 2015/8/1.
 */
public interface DynamicConfValidator {
    public boolean validator(Object value,NodeEvent.Type nodeEventType);
}
