package net.automaters.util.formats;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {

    public static String formatTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
