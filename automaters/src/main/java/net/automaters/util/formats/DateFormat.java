package net.automaters.util.formats;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class DateFormat {
    public static String formatDateToday() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return LocalDate.now().format(formatter);
    }
}
