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

@XmlRootElement(name = "alertmetrics")
public class Alertmetrics {
	private String name;
	private String upLimit;
	private String lowLimit;
	
	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
	public String getUpLimit() {
		return upLimit;
	}
	@XmlElement
	public void setUpLimit(String upLimit) {
		this.upLimit = upLimit;
	}
	public String getLowLimit() {
		return lowLimit;
	}
	@XmlElement
	public void setLowLimit(String lowLimit) {
		this.lowLimit = lowLimit;
	}
	
}
