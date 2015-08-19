import java.util.Date;

/**
 * Created by wz68 on 2015/7/29.
 */
public class DefaultTest {
    public static void main(String[] args) {
        long x =1437485467586L;
        Date d = new Date();
        d.setTime(x);
        System.out.println(d);
    }
}
