import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {

    public Time() {
    }

    public static void print() {
        java.util.Date date = new java.util.Date();
        System.out.println(date);
    }
    public static void printFR() {
        String timeStamp = new SimpleDateFormat("EEE dd MMM hh:mm:ss yyyy",Locale.FRANCE).format(new Date());
        System.out.println(timeStamp);
    }
}
