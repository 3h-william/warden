package newegg.ec.warden.watch.itemservice.faq.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Service实体，一个Service包含一个名称和多个ServiceGroup。
 * 
 * @company 新蛋信息技术（中国）有限公司
 * 
 * @author Sunshine.W.Wang
 * 
 * @date 2012-10-23
 */

@XmlRootElement(name = "alert")
public class Alert {
	private List<Alertmetrics> alertMetrics;

	public List<Alertmetrics> getAlertMetrics() {
		return alertMetrics;
	}
	@XmlElement(name = "alertmetrics")
	public void setAlertMetrics(List<Alertmetrics> alertMetrics) {
		this.alertMetrics = alertMetrics;
	}
}
