package newegg.ec.disnotice.core.pathsystem;

import newegg.ec.disnotice.core.NoticeCoreConstant;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.conf.PathConfiguration;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Manager of path
 * <p/>
 * see Issue <line>http://trgit2/wz68/warden/issues/17</line>
 * <p/>
 * Created by wz68 on 2015/7/8.
 */
public class DisPathManager implements NoticeCoreConstant {
    private static Logger log = WardenLogging.getLog(DisPathManager.class);
    private static Properties prop = new Properties();
    private static PathConfiguration pc;

    private static final String default_dis_Root = "disRoot";
    private static final String default_dis_version = "0.1";
    private static final String default_dis_location = "local";

    private static final String DISNOTICE_ROOT_PATH = "disNotice.root.path";
    private static final String DISNOTICE_VERSION = "disNotice.version";
    private static final String DISNOTICE_LOCATION = "disNotice.location";

    private static final String FileDelimiter = "/";


    static {
        InputStream is = DisPathManager.class.getClassLoader().getResourceAsStream("disNotice-core.properties");
        if (null == is) {
            System.err.println("disNotice-core.properties not found , exit system");
            System.exit(-1);
        }
        try {
            prop.load(is);
            pc = new PathConfiguration();
            pc.rootPath = prop.getProperty(DISNOTICE_ROOT_PATH, default_dis_Root);
            pc.disVersion = prop.getProperty(DISNOTICE_VERSION, default_dis_version);
            pc.location = prop.getProperty(DISNOTICE_LOCATION, default_dis_location);
        } catch (IOException e) {
            log.error("init disPath error,",e);
            System.err.println("load disNotice-core.properties error, exit system");
            System.exit(-1);
        }
    }

    public static String getRootPath() {
        return pc.rootPath + "_" + pc.disVersion;
    }

    /**
     * ${DisRootVersion}/${location}/${type}_${scope}_${name}
     *
     * @param inC
     * @return
     */
    public static String getInstancePath(InstanceConfiguration inC) {
        String disRootVersion = getRootPath();
        String location = pc.location;
        String instanceDataPath = inC.getInstancePathType() + "_" + inC.getInstancePathScope() + "_" + inC.getInstanceName();
        return contactFilePath(disRootVersion, location, instanceDataPath);
    }

    private static String contactFilePath(String... args) {
        String resPath = "";
        for (int i = 0; i < args.length; i++) {
            //last not add FileDelimiter
            if (i == args.length - 1) {
                resPath += args[i];
            } else {
                resPath += args[i] + FileDelimiter;
            }
        }
        return resPath;
    }
}


