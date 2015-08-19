package newegg.ec.disnotice.core.collections;

import newegg.ec.disnotice.core.NodeEvent;

public interface DisNoticeMapChangedListener<T> extends DataChangedListener {
    public abstract void  dataChangedEvent(DisNoticeMap<T> disNoticeMap , String ChangeKeyName , NodeEvent.Type nodeEventType);
}