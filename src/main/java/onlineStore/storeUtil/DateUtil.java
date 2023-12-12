package onlineStore.storeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtil {

    private static final Date date = new Date();

    public static Date stringToDate(String dateFormat) {
        try {
            return SDF.parse(dateFormat);
        } catch (ParseException e) {
            System.out.println("Wrong date format");
        }
        return date;
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd:MM:yyyy HH:mm");
    public static String dateFormat = SDF.format(date);
}
