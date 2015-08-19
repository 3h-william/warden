package newegg.ec.warden.watch.itemservice.faq.helper;

import java.util.HashMap;
import java.util.Map;

import com.newegg.ec.common.config.JAXBUtil;
import com.newegg.ec.common.config.monitor.IConfigChangedListener;
import com.newegg.ec.common.config.register.Config;
import com.newegg.ec.common.log.Logger;
import newegg.ec.warden.watch.itemservice.faq.config.MonitorConfig;

/**
 * 解析MonitorConfig并提供公开api获取MonitorConfig对象
 *
 * @company 新蛋信息技术（中国）有限公司
 *
 * @author Sunshine.W.Wang
 *
 * @date 2012-10-23
 */
public class MonitorConfigHelper  implements IConfigChangedListener{

	private static final Logger log = Logger
			.getLogger(MonitorConfigHelper.class);

	/**
	 *用来对MonitorConfig进行缓存的map
	 */
	private static Map<String, MonitorConfig> configMap = new HashMap<String, MonitorConfig>();

	/**
	 * 从指定的path获取文件初始化MonitorConfig对象
	 *
	 * @param configPath
	 *            config文件地址
	 * @param forceCreate
	 *            强制创建一个新的
	 * @return MonitorConfig
	 */
	public static MonitorConfig getMonitorConfig(String configPath,
			boolean forceCreate) {
		MonitorConfig config = configMap.get(configPath);
		try {
			if (config == null || forceCreate) {
				config = (MonitorConfig) JAXBUtil.xml2java(MonitorConfig.class
						.getName(), configPath);
				config.sort();// sort it..
				configMap.put(configPath, config);
			}else{
				log.info("Cache hited in loading config "+configPath);
			}
		} catch (Exception e) {
			log.error("Error happens in init MonitorConfig.", e);
		}
		return config;
	}

	/**
	 * 获取MonitorConfig对象，不强制创建新的
	 *
	 * @return MonitorConfig
	 */
	public static MonitorConfig getMonitorConfig(String configName) {
		return getMonitorConfig(configName, false);
	}


	/**
	 * 使用指定的文件名，重新加载配置文件，重新创建MonitorConfig对象
	 *
	 * @return MonitorConfig
	 */
	public static MonitorConfig reloadMonitorConfig(String config) {
		return getMonitorConfig(config, true);
	}

	public void configChanged(Config config) {
		configMap.clear();
		log.info("Cache cleared, config reloaded.");
	}

}
