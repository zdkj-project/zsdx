import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 *
 * @description:
 * @author: yz
 * @date: 2018-09-26
 * @time: 14:32
 */
public class DateTest {
    public static void main(String[] arge) throws ParseException {
      /*  Date date = new Date();
        //获取当前时间判断是 上午还是下午
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour);*/
       /* SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String toDate ="2018-05-01 12:50:00";
        long from = new Date().getTime();
        long to = simpleFormat.parse(toDate).getTime();
        int minutes = (int) ((to - from)/(1000 * 60));
        System.out.println(minutes);*/
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = System.currentTimeMillis() - simpleFormat.parse("2018-09-26 12:00:00").getTime();
        int days = (int) diff / (1000 * 60);
        System.out.println(days);
        }
}
