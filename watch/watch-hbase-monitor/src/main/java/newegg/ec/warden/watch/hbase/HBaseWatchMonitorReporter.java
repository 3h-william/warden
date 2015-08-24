package newegg.ec.warden.watch.hbase;

import com.newegg.ec.warden.watch.BaseWatchMonitorReporter;
import newegg.ec.disnotice.core.collections.DisNoticeMap;
import newegg.ec.disnotice.core.conf.InstanceConfiguration;
import newegg.ec.disnotice.core.pathsystem.DisPathManager;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.disnotice.tool.dynamicconf.DynamicConfigurationFactory;
import newegg.ec.disnotice.tool.dynamicconf.validator.IntegerValueValidator;
import newegg.ec.disnotice.tool.zk.ZKConnections;
import newegg.ec.warden.PropertyLoader;
import newegg.ec.warden.WardenLogging;
import org.slf4j.Logger;

/**
 * This class for record some status if HBase status changed.
 * Strategy is very important in this case.
 * <p/>
 * Created by wz68 on 2015/7/14.
 */
public class HBaseWatchMonitorReporter extends BaseWatchMonitorReporter implements HBaseMonitorConstants {
    private static Logger log = WardenLogging.getLog(HBaseWatchMonitorReporter.class);

    public HBaseWatchMonitorReporter(PropertyLoader propertyLoader) throws Exception {
        super(propertyLoader);
    }

    /**
     * False = > True  should in some latency
     * True = > False should immediately
     *
     * @param tableName
     * @param status
     */
    public void setTableStatus(String tableName, boolean status) {
        String wrapTableName = wrapTableReportName(tableName);
        String preStat = this.dnm.get(wrapTableName);
        // if now is true
        if (status) {
            if (null != preStat && preStat.equalsIgnoreCase("True")) {
                // do nothing
            } else {
                // latency for changed status in this condition False=>True
                latencyForValueChanged(wrapTableName, "True");
            }
        }
        // if now is false
        else {
            if (null != preStat && preStat.equalsIgnoreCase("False")) {
                // do nothing
            } else {
                this.dnm.put(wrapTableName, "False");
            }
            recoverLatencyRecord(wrapTableName);
        }
    }

    /**
     * input: tableName = IM_ItemBase
     * output : return = table.im_itembase.status
     *
     * @param tableName
     * @return
     */
    private String wrapTableReportName(String tableName) {
        String lowerCase = tableName.toLowerCase();
        return "table." + lowerCase + ".status";
    }
}


