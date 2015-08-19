package newegg.ec.disnotice.business.dao.util;

/**
 * @author William Zhu
 * 
 */
public class DefineParameter {
    public static final int sqliteTimeout=500;
	public static final int historyRowNums=1000;
	
	public enum CompareOption {
		/** less than */
		LESS,
		/** less than or equal to */
		LESS_OR_EQUAL,
		/** equals */
		EQUAL,
		/** not equal */
		NOT_EQUAL,
		/** greater than or equal to */
		GREATER_OR_EQUAL,
		/** greater than */
		GREATER,
		/** no operation */
		NO_OP,
	}
}