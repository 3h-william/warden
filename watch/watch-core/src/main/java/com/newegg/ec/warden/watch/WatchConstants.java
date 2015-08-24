package com.newegg.ec.warden.watch;

/**
 * Created by wz68 on 2015/8/18.
 */
public interface WatchConstants {
    public static final String DISNOTICE_ZK_CONNECT_SERVERS = "disnotice.zk.connect.servers";
    /**
     *  name of this watch app
     */
    public static final String WATCH_INSTANCE_NAME = "watch.app.name";

    /**
     * name of report
     */
    public static final String WATCH_REPORT_NAME = "watch.report.name";

    /**
     * each task of watching interval
     */
    public static final String WATCH_INTERVAL_SECONDS = "watch.interval.seconds";

    /**
     * if use thread pools and future to get this task , this value is to set the timeout for the future task in "get"
     */
    public static final String ASYNC_TASK_PROCESSOR_TIMEOUT_SECONDS = "async.task.processor.timeout.seconds";

    /**
     * latency seconds for status changed
     */
    public static final String STATUS_CHANGED_LATENCY_SECONDS = "status.changed.latency.seconds";

    // report instance statistics path
    public static final String WATCH_STATISTICS_MAP_NAME = "watch.statistics.map.name";
}
