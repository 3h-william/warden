package newegg.ec.warden.watch.itemservice.faq.config;

import newegg.ec.warden.watch.itemservice.faq.helper.ServiceUtil;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * MonitorConfig配置类
 * 
 * @company 新蛋信息技术（中国）有限公司
 * 
 * @author Sunshine.W.Wang
 * 
 * @date 2012-10-23
 */

@XmlRootElement(name = "MonitorConfig")
public class MonitorConfig {
	private List<Service> service;

	private int timeOut;
	private Alert alert;
	
	//状态改变发送邮件服务是否开启
	private String isOpenStatusChanged;
    
	public String getIsOpenStatusChanged() {
		return isOpenStatusChanged;
	}
	@XmlAttribute
	public void setIsOpenStatusChanged(String isOpenStatusChanged) {
		this.isOpenStatusChanged = isOpenStatusChanged;
	}

	public List<Service> getService() {
		return service;
	}

	@XmlElement
	public void setService(List<Service> service) {
		this.service = service;
	}

	public int getTimeOut() {
		return timeOut;
	}

	@XmlAttribute
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	
	public Alert getAlert() {
		return alert;
	}
	@XmlElement
	public void setAlert(Alert alert) {
		this.alert = alert;
	}
	

	/**
	 * 对Config内容进行排序
	 */
	public void sort() {
		ServiceUtil.sort(service);
	}
	
	@Override
	public String toString() {
		return " IsOpenStatusChanged = "+isOpenStatusChanged;
	}
}
