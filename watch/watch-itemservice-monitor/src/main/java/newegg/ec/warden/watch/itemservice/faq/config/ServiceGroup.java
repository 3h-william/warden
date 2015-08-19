package newegg.ec.warden.watch.itemservice.faq.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ServiceGroup实体，一个ServiceGroup包含一个名称和多个Service URL地址。
 * 
 * @company 新蛋信息技术（中国）有限公司
 * 
 * @author Sunshine.W.Wang
 * 
 * @date 2012-10-23
 */

@XmlRootElement(name = "serviceGroup")
public class ServiceGroup {
	private String group;

	private List<String> serviceUrl;

	public String getGroup() {
		return group;
	}

	@XmlAttribute
	public void setGroup(String group) {
		this.group = group;
	}

	public List<String> getServiceUrl() {
		return serviceUrl;
	}

	@XmlElement
	public void setServiceUrl(List<String> serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

}
