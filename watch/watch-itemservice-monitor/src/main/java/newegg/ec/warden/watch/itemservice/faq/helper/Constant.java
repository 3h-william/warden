package newegg.ec.warden.watch.itemservice.faq.helper;

/**
 * 系统常量集合
 *
 * @company 新蛋信息技术（中国）有限公司
 *
 * @author Sunshine.W.Wang
 *
 * @date 2012-10-23
 */
public class Constant {
	public static final String FAQ = "FAQ.jsp";
	/**ActionServlet path of JPF and ItemdetailService.*/
	public static final String ACTION = "servlet/action";
	public static final String LAUNCH = "launch.jsp";
	public static final String SERVICE_DOWN = "Down";
	public static final String TIME_OUT = "timeout";
	public static final String NETSCALER_STATUS_OK="ServiceStatusOK";
	/**
	 * 默认的超时时间60秒
	 */
	public static final int DEFAULT_TIME_OUT = 60;
	/**
	 * 默认OpenStatusChanged服务开启
	 */
	public static final boolean DEFAULT_IsOpenStatusChanged=true;

	/**
	 * Servlet参数，标志使用哪个配置文件，默认使用monitor.xml
	 */
	public static final String PARAM_WHICH_CONFIG= "config";

	/**
	 * 默认的config文件名称 monitor.xml
	 */
	public static final String DEFAULT_CONFIG_FILE = "monitor.xml";

	/**
	 * Servlet参数，操作类型
	 */
	public static final String PARAM_OPERATION = "op";
	/**
	 * servlet参数，获取状态的url值
	 */
	public static final String PARAM_SERVICE_URL = "url";
	/**
	 * PARAM_OPERATION参数的一个值，表示获取service列表
	 */
	public static final String PARAM_VALUE_GET_SERVICE_LIST = "service";
	/**
	 * PARAM_OPERATION参数的一个值，表示获取某个url的状态
	 */
	public static final String PARAM_VALUE_GET_SERVICE_URL_STATUS = "status";
	/**
	 * reload config initial delay in minute unit.
	 */
	public static final long SCHEDULE_RELOAD_CONFIG_INITIAL_DELAY = 5;
	/**
	 * reload config every period in minute unit.
	 */
	public static final long SCHEDULE_RELOAD_CONFIG_PERIOD = 5;

	/**
	 * PARAM_OPERATION参数的一个值，对指定的service进行offline
	 */
	public static final String PARAM_ACTION_OFFLINE = "offline";

	/**
	 * PARAM_OPERATION参数的一个值，对指定的service进行online
	 */
	public static final String PARAM_ACTION_ONLINE = "online";
	/**
	 * PARAM_OPERATION参数的一个值，开启某个URL的common-log
	 */
	public static final String PARAM_ACTION_ENABLE_COMMON_LOG = "enable_common_log";
	/**
	 * PARAM_OPERATION参数的一个值，关闭某个URL的common-log
	 */
	public static final String PARAM_ACTION_DISABLE_COMMON_LOG = "disable_common_log";
	/**
	 * PARAM_OPERATION参数的一个值，清空common-log的cache
	 */
	public static final String PARAM_ACTION_CLEAR_LOG_CACHE = "clear_common_log_cache";

}
