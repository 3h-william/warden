package newegg.ec.disnotice.business.dao.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author William Zhu
 * 
 * sqlite db is not support concurrence,so we should control concurrence in our program
 */
public class LockControl {

    private static Lock writeLock = new ReentrantLock();
	public static  void lockWriteTransaction(){
		writeLock.lock();
	}
	public static  void unlockWriteTransaction(){
		writeLock.unlock();
	}
}