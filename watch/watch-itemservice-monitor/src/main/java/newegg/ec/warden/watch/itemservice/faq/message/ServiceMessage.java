package newegg.ec.warden.watch.itemservice.faq.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个URL的状态
 *
 * @company 新蛋信息技术（中国）有限公司
 *
 * @author Sunshine.W.Wang
 *
 * @date 2012-10-23
 */
public class ServiceMessage {
	private String url;
	private String serviceStatus;
	private String statusList;
	private String version;
	private String isCommonLogEnabled;
	private boolean isOkOnNetScaler;
	private String rawMessage;
	
	// for itemserivce
	private String requestCount;
	private String singleCount;
	private String cacheHit;
	private String cacheRate;
	private String avgResponse;
	private String latestResponse;
	
	public String getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(String requestCount) {
		this.requestCount = requestCount;
	}

	public String getSingleCount() {
		return singleCount;
	}

	public void setSingleCount(String singleCount) {
		this.singleCount = singleCount;
	}

	public String getCacheHit() {
		return cacheHit;
	}

	public void setCacheHit(String cacheHit) {
		this.cacheHit = cacheHit;
	}

	public String getCacheRate() {
		return cacheRate;
	}

	public void setCacheRate(String cacheRate) {
		this.cacheRate = cacheRate;
	}

	public String getAvgResponse() {
		return avgResponse;
	}

	public void setAvgResponse(String avgResponse) {
		this.avgResponse = avgResponse;
	}

	public String getLatestResponse() {
		return latestResponse;
	}

	public void setLatestResponse(String latestResponse) {
		this.latestResponse = latestResponse;
	}

	
	private static Map<String,String> lastStatus=new HashMap<String,String>();
	// 并发访问量
	private String concurrentUserCount = "";

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getStatusList() {
		return statusList;
	}

	public void setStatusList(String statusList) {
		this.statusList = statusList;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getConcurrentUserCount() {
		return concurrentUserCount;
	}

	public void setConcurrentUserCount(String concurrentUserCount) {
		this.concurrentUserCount = concurrentUserCount;
	}

	public String getIsCommonLogEnabled() {
		return isCommonLogEnabled;
	}

	public void setIsCommonLogEnabled(String isCommonLogEnabled) {
		this.isCommonLogEnabled = isCommonLogEnabled;
	}

	public boolean isOkOnNetScaler() {
		return isOkOnNetScaler;
	}

	public void setOkOnNetScaler(boolean isOkOnNetScaler) {
		this.isOkOnNetScaler = isOkOnNetScaler;
	}

	public String getRawMessage() {
		return rawMessage;
	}

	public void setRawMessage(String rawMessage) {
		this.rawMessage = rawMessage;
	}
	

	public static Map<String, String> getLastStatus() {
		return lastStatus;
	}

	public static void setLastStatus(Map<String, String> lastStatus) {
		ServiceMessage.lastStatus = lastStatus;
	}

	@Override
	public String toString() {

		return "IsOkOnNetScaler:" + isOkOnNetScaler + "\tServiceStatus:" + serviceStatus + "\tStatusList" + statusList
				+ "\tVersion:" + version + "\turl:" + url+"\tIsCommonLogEnabled:"+isCommonLogEnabled;
	}
}
