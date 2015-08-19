package newegg.ec.warden;

/**
 * Created by wz68 on 2015/7/8.
 */
public class WardenLogging {

    public static String logName(Class clazz) {
        return clazz.getName();
    }

    public static org.slf4j.Logger getLog(Class clazz) {
        return org.slf4j.LoggerFactory.getLogger(logName(clazz));
    }
}
