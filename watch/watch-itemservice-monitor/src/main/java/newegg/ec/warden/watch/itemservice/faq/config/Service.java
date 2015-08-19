package newegg.ec.warden.watch.itemservice.faq.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
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

@XmlRootElement(name = "service")
public class Service {
	private String name;
	private List<ServiceGroup> serviceGroup;

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	public List<ServiceGroup> getServiceGroup() {
		return serviceGroup;
	}

	@XmlElement
	public void setServiceGroup(List<ServiceGroup> serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

}
