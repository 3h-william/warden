package newegg.ec.warden;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wz68 on 2015/7/9.
 */
public class PropertyLoader {
    private static Logger log = WardenLogging.getLog(PropertyLoader.class);
    private Properties prop;

    /**
     * if propName is null ,just new prop as empty instance
     *
     * @param propName
     */
    public PropertyLoader(String propName) {
        if(null==propName){
            prop = new Properties();
        }else{
            loadPropertiesFromClasspath(propName);
        }

    }

    public Properties getProp() {
        return prop;
    }

    public String getValue(String key, String defaultString) {
        return this.prop.getProperty(key, defaultString);
    }

    public String getValue(String key) {
        return this.prop.getProperty(key);
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    private void loadPropertiesFromClasspath(String propFileName) {
        log.info("load properties from classpath:" + propFileName);
        InputStream is = PropertyLoader.class.getClassLoader().getResourceAsStream(propFileName);
        if (null == is) {
            System.err.println(propFileName + " not found , exit system");
            System.exit(-1);
        }
        try {
            prop = new Properties();
            prop.load(is);
            log.info("load over");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("load disNotice-core.properties error, exit system");
            System.exit(-1);
        }
    }
}
