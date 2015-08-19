package newegg.ec.warden.watch.itemservice.faq.helper;

import java.net.SocketTimeoutException;
import java.net.URL;

import newegg.ec.warden.watch.itemservice.faq.message.ServiceMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



//import org.apache.log4j.*;
import com.newegg.ec.common.log.Logger;

/**
 * 通过访问url地址，获取内容，并解析出ServiceMessage对象的工具类
 * 
 * @company 新蛋信息技术（中国）有限公司
 * 
 * @department SH-MIS EC BigData
 * 
 * @author jc3w
 * 
 * @date 2013-3-6
 */
public class ServiceMessageParser {
	private static final Logger log = Logger
			.getLogger(ServiceMessageParser.class);
	/**
	 * 对FAQ页面进行解析
	 * 
	 * @param serviceUrl
	 * @return
	 */
	public static ServiceMessage get(String serviceUrl, int timeout) {
		ServiceMessage serviceMessage = new ServiceMessage();
		serviceMessage.setUrl(serviceUrl);
		if (serviceUrl == null || "".equals(serviceUrl.trim())) {
			return serviceMessage;
		}
		// 根据url获得jsp的body数据
		String faqBodyMsg = getBodyMsg(getServiceUrl(serviceUrl, true), timeout);
		if (faqBodyMsg == null) {
			return serviceMessage;
		}
		// 如果访问不到，认为service down掉
		if (Constant.SERVICE_DOWN.equalsIgnoreCase(faqBodyMsg)) {
			serviceMessage.setServiceStatus(Constant.SERVICE_DOWN);
			serviceMessage.setStatusList("");
		} else if (Constant.TIME_OUT.equalsIgnoreCase(faqBodyMsg)) {// 访问超时
			serviceMessage.setServiceStatus(Constant.TIME_OUT);
			serviceMessage.setStatusList("");
		} else {
			ServiceMessage msg = parseFAQBodyString(faqBodyMsg);
			if (msg != null) {
				msg.setUrl(serviceUrl);
				serviceMessage = msg;
			}
		}
		if (serviceMessage.getServiceStatus() == null) {
			return serviceMessage;
		}
		
		// 获得launch页面信息
		String launchBodyMsg = getBodyMsg(getServiceUrl(serviceUrl, false),
				timeout);
		// 奖launch页面version信息注入
		setServiceVersion(launchBodyMsg, serviceMessage);

		return serviceMessage;
	}
	

	/**
	 * 获得所有<dt>元素
	 * 
	 * @param bodyMsg
	 * @return
	 */
	private static Elements getAllDtElements(String bodyMsg) {
		Document doc = Jsoup.parse(bodyMsg);
		Elements elements = doc.select("dt");
		return elements;
	}

	/**
	 * 获得第一个<dt>元素
	 * 
	 * @param elements
	 * @return
	 */
	private static Element getFirstDtElement(Elements elements) {
		Element element = elements.get(0);
		if (element != null) {
			element = element.nextElementSibling();
			if (element != null) {
				return element;
			}
		}

		return null;
	}

	/**
	 * 将version信息注入ServiceMessage
	 * 
	 * @param bodyMsg
	 * @param msg
	 */
	private static void setServiceVersion(String bodyMsg, ServiceMessage msg) {
		String version = "";
		if (msg == null)
			return;
		Elements elements = getAllDtElements(bodyMsg);
		if (elements == null || elements.size() == 0) {
			msg.setVersion(version);
			return;
		}
		Element element = getFirstDtElement(elements);
		if (element != null) {
			msg.setVersion(element.text() == null ? "" : element.text());
		}
	}

	/**
	 * 解析body信息
	 */
	private static ServiceMessage parseFAQBodyString(String bodyMsg) {
		ServiceMessage msg = new ServiceMessage();
		msg.setRawMessage(bodyMsg);
		msg.setOkOnNetScaler(bodyMsg.contains(Constant.NETSCALER_STATUS_OK));
		Elements elements = getAllDtElements(bodyMsg);
		if (elements == null || elements.size() == 0)
			return null;
		Element element = getFirstDtElement(elements);
		if (element != null) {
			msg.setServiceStatus(element.text());
		}

		if (elements.size() >= 2) {
			element = elements.get(1);
			if (element != null) {
				StringBuffer statusList = new StringBuffer("");
				while (element.nextElementSibling() != null) {
					element = element.nextElementSibling();
					statusList.append(element);
				}
				msg.setStatusList(statusList.toString());
			}

			if (elements.size() >= 3) {
				// Concurrent User Count
				element = elements.get(2);
				if (element != null) {
					String concurrentUserCountStr = element
							.nextElementSibling() == null ? "" : element
							.nextElementSibling().text();
					if (!"".equals(concurrentUserCountStr.trim())) {
						msg.setConcurrentUserCount(concurrentUserCountStr);
						//判断HBase和SQL SERVER的状态是否都是OK,如果是ServiceStatus为OK，否则是FAILED
						if("OK".equals(msg.getConcurrentUserCount())&&"OK".equals(msg.getServiceStatus())){
							msg.setServiceStatus("OK");
							
						}else{
							msg.setServiceStatus("FAILED");
						}
					}
				}
				if (elements.size() >= 4) {
					element = elements.get(3);
					if (element != null) {
						String commonLogEnabled = element.nextElementSibling() == null ? ""
								: element.nextElementSibling().text();
						if (!"".equals(commonLogEnabled.trim())) {
							msg.setIsCommonLogEnabled(commonLogEnabled);
						}
					}
				}
			}
		}

		return msg;
	}

	/**
	 * 使用Jsoup获得jsp的body信息
	 * 
	 * @param url
	 * @return
	 */
	public static String getBodyMsg(String url, int timeout) {
		long startTime = System.currentTimeMillis();
		String bodyMsg = null;
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL(url), 1000 * timeout);
		} catch (SocketTimeoutException ste) {
			bodyMsg = Constant.TIME_OUT;
			log.info(url + " " + Constant.TIME_OUT);
		} catch (Exception e) {
			bodyMsg = Constant.SERVICE_DOWN;
			log.info(url + " " + Constant.SERVICE_DOWN + " " + e.toString());
		}

		if (doc != null) {
			bodyMsg = doc.body().toString();
		}

		long endTime = System.currentTimeMillis();
		log.info("Get Body Message cost time:" + (endTime - startTime));
		return bodyMsg;
	}

	/**
	 * 拼接url信息信息 url+FAQ.jsp url+launch.jsp
	 */
	private static String getServiceUrl(String url, boolean FAQFlag) {
		String result = null;
		if (url != null && url.length() > 0) {
			if (url.contains(Constant.FAQ)) {
				result = url;
			}

			boolean endFlag = url.endsWith("/");
			if (!endFlag) {
				url += "/";
			}

			if (FAQFlag) {
				result = url + Constant.FAQ;
			} else {
				result = url + Constant.LAUNCH;
			}
		}

		return result;
	}
}
