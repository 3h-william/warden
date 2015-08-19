package newegg.ec.warden.watch.itemservice.faq.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.newegg.ec.common.log.Logger;
import newegg.ec.warden.watch.itemservice.faq.config.Service;
import newegg.ec.warden.watch.itemservice.faq.config.ServiceGroup;

/**
 * Service工具类，提供对service集合排序功能等
 * 
 * @company 新蛋信息技术（中国）有限公司
 * 
 * @department SH-MIS EC BigData
 * 
 * @author jc3w
 * 
 * @date 2013-3-6
 */
public class ServiceUtil {

	private static final Logger log = Logger.getLogger(ServiceUtil.class);

	/**
	 * 对Service集合深入排序，asc order
	 * 
	 * @param services
	 *            待排序的集合
	 */
	public static final void sort(List<Service> services) {
		// sort groups
		for (Service s : services) {
			List<ServiceGroup> groups = s.getServiceGroup();
			// sort urls
			for (ServiceGroup g : groups) {
				List<String> urls = g.getServiceUrl();
				Collections.sort(urls);
				log.debug(Arrays.toString(urls.toArray()));
			}
			Collections.sort(groups, new Comparator<ServiceGroup>() {
				public int compare(ServiceGroup o1, ServiceGroup o2) {
					return o1.getGroup().compareTo(o2.getGroup());
				}
			});
		}
	}

}
