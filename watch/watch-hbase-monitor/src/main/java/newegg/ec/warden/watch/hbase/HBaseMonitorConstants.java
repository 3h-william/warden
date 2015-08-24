package newegg.ec.warden.watch.hbase;

import com.newegg.ec.warden.watch.WatchConstants;

/**
 * Created by wz68 on 2015/8/1.
 */
public interface HBaseMonitorConstants extends WatchConstants{
    /**
     * key cache update interval seconds
     */
    public static final String KEY_CACHE_UPDATE_INTERVAL_SECONDS = "key.cache.update.interval.seconds";

    /**
     * if get keys from specify table time costs > this value, then will set false for this table
     */
    public static final String WATCH_TIMEOUT_MILLISECOND = "watch.timeout.millisecond";
}
