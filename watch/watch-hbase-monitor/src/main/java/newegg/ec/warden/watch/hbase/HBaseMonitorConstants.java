package newegg.ec.warden.watch.hbase;

/**
 * Created by wz68 on 2015/8/1.
 */
public interface HBaseMonitorConstants {
    public static final String DISNOTICE_ZK_CONNECT_SERVERS = "disnotice.zk.connect.servers";
    public static final String WATCH_INSTANCE_NAME = "watch.instance.name";
    public static final String SERVICE_CONF_NAME = "service.conf.name";

    /**
     * if get keys from specify table time costs > this value, then will set false for this table
     */
    public static final String WATCH_TIMEOUT_MILLISECOND = "watch.timeout.millisecond";
    /**
     * each task of watching table interval
     */
    public static final String WATCH_INTERVAL_SECONDS = "watch.interval.seconds";
    /**
     * key cache update interval seconds
     */
    public static final String KEY_CACHE_UPDATE_INTERVAL_SECONDS = "key.cache.update.interval.seconds";
    /**
     * if use thread pools and future to get this task , this value is to set the timeout for the future task in "get"
     */
    public static final String ASYNC_TASK_PROCESSOR_TIMEOUT_SECONDS = "async.task.processor.timeout.seconds";

    /**
     * latency seconds for status changed
     */
    public static final String STATUS_CHANGED_LATENCY_SECONDS = "status.changed.latency.seconds";
}
