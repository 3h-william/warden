package newegg.ec.disnotice.core.collections;

import newegg.ec.disnotice.core.NodeEvent;
import newegg.ec.disnotice.core.zk.ZKManager;
import newegg.ec.warden.WardenLogging;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by wz68 on 2015/7/11.
 * <p/>
 * defined special map for disNotice of ZK
 */
public class DisNoticeMap<T> implements Map<String, T> {
    private static Logger log = WardenLogging.getLog(DisNoticeMap.class);
    protected String rootPath = null;
    protected Map<String, T> mapData = new ConcurrentHashMap<String, T>();
    protected Class valueClass;
    protected ZKManager zkmgr;
    protected Object listenerObject;
    protected DisNoticeMapChangedListener<T> disNoticeMapChangedListener;

    public enum DisNoticeMapModel {
        READ, WRITE, RW
    }

    public void addDataChangedListener(DisNoticeMapChangedListener<T> disNoticeMapChangedListener) {
        this.disNoticeMapChangedListener = disNoticeMapChangedListener;
    }

    public <T> DisNoticeMap(ZKManager zkmgr, String rootPath, Class<T> valueClass, DisNoticeMapModel disNoticeMapModel) throws Exception {
        this.rootPath = rootPath;
        this.valueClass = valueClass;
        this.zkmgr = zkmgr;

        // write model
        if (disNoticeMapModel.equals(DisNoticeMapModel.WRITE) || disNoticeMapModel.equals(DisNoticeMapModel.RW)) {
            // check root node exist
            if (!zkmgr.existPath(rootPath)) {
                log.info("rootPath is null , initialize create rootpath : =" + rootPath);
                zkmgr.createPath(rootPath, "auto created".getBytes(), CreateMode.PERSISTENT);
            }
        }
        // read model
        if (disNoticeMapModel.equals(DisNoticeMapModel.READ) || disNoticeMapModel.equals(DisNoticeMapModel.RW)) {
            DisNoticeMapListenerCallBack disNoticeMapListenerCallBack = new DisNoticeMapListenerCallBack();
            this.listenerObject = zkmgr.startTreeListener(rootPath, disNoticeMapListenerCallBack);
            // wait for init map
            log.info("initialized map start :" + rootPath);
            disNoticeMapListenerCallBack.waitForInitialized();
            log.info("initialized map over : " + rootPath);
        }
    }

    public void invokeDataChangedEvent(String keyName, NodeEvent.Type nodeEventType) {
        if (null != disNoticeMapChangedListener)
            disNoticeMapChangedListener.dataChangedEvent(this, keyName, nodeEventType);
    }

    public void close() throws Exception {
        if (null != listenerObject) {
            zkmgr.stopTreeListener(listenerObject);
        }
    }

    public String getRootPath() {
        return rootPath;
    }

    @Override
    public int size() {
        return mapData.size();
    }

    @Override
    public boolean isEmpty() {
        return mapData.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return mapData.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return mapData.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return mapData.get(key);
    }

    @Override
    public T put(String key, T value) {
        T preValue = this.mapData.get(key);
        this.mapData.put(key, value); // put to local immediately
        try {
            String path = rootPath + "/" + key;
            zkmgr.updatePath(path, parseDataToByte(value));
        } catch (Exception e) {
            log.error("faild to put data, " + value + ",", e);
        }
        return preValue;
    }

    @Override
    public T remove(Object key) {
        T preValue = this.mapData.get(key);
        String path = rootPath + "/" + key;
        zkmgr.deleteIfExist(path);
        this.mapData.remove(key);
        return preValue;
    }

    @Override
    public void putAll(Map<? extends String, ? extends T> m) {
        throw new RuntimeException("not support function");
    }

    @Override
    public void clear() {
        mapData.clear();
    }

    @Override
    public Set<String> keySet() {
        return mapData.keySet();
    }

    @Override
    public Collection<T> values() {
        return mapData.values();
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        return mapData.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return mapData.equals(o);
    }

    @Override
    public int hashCode() {
        return mapData.hashCode();
    }

    private T parseDataFromByte(byte[] data) throws UnsupportedEncodingException {
        if (valueClass.equals(java.lang.String.class)) {
            return (T) new String(data, "UTF-8");
        } else {
            throw new RuntimeException("class type not support ,type=" + valueClass);
        }
    }

    private byte[] parseDataToByte(T data) throws UnsupportedEncodingException {
        if (valueClass.equals(java.lang.String.class)) {
            String value = (String) data;
            return value.getBytes();
        } else {
            throw new RuntimeException("class type not support ,type=" + valueClass);
        }
    }

    class DisNoticeMapListenerCallBack implements ZKManager.TreeListenerCallback {
        protected Object initialized = new Object();

        @Override
        public void node_add(String path, byte[] value) throws Exception {
            if (!shouldIgnoreChanged(path)) {
                String keyName = ZKPaths.getNodeFromPath(path); //TODO , should not reply on curator directly
                T t = parseDataFromByte(value);
                mapData.put(keyName, t);
                invokeDataChangedEvent(keyName, NodeEvent.Type.ADD);
            }
        }

        @Override
        public void node_update(String path, byte[] value) throws Exception {
            if (!shouldIgnoreChanged(path)) {
                String keyName = ZKPaths.getNodeFromPath(path); //TODO , should not reply on curator directly
                T t = parseDataFromByte(value);
                mapData.put(keyName, t);
                invokeDataChangedEvent(keyName, NodeEvent.Type.UPDATE);
            }
        }

        @Override
        public void node_removed(String path) throws Exception {
            if (!shouldIgnoreChanged(path)) {
                String keyName = ZKPaths.getNodeFromPath(path); //TODO , should not reply on curator directly
                mapData.remove(keyName);
                invokeDataChangedEvent(keyName, NodeEvent.Type.REMOVE);
            }
        }

        @Override
        public void node_initialized() throws Exception {
            log.info("inialized over event");
            synchronized (initialized) {
                initialized.notify();
            }
        }

        @Override
        public void other_event(String eventName) {
            log.info("other event name = " + eventName);
        }

        public void waitForInitialized() throws InterruptedException {
            synchronized (initialized) {
                initialized.wait();
            }
        }

        private boolean shouldIgnoreChanged(String path) {
            if (path.equals(rootPath)) {
                return true;
            }
            String operatePath = StringUtils.substringAfter(path, rootPath);
            int counts = StringUtils.countMatches(operatePath, "/");
            if (counts > 2) {  // 2 means such path node as  /*/*/*....  if the number of slash more than 2 ,means the node can be ignored
                return true;
            }
            return false;
        }

        private boolean isRootRemove(String path) {
            return path.equals(rootPath);
        }
    }
}



