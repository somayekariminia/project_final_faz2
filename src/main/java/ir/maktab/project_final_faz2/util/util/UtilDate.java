package ir.maktab.project_final_faz2.util.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

public class UtilDate {
    public static Date changeLocalDateToDate(LocalDateTime localDate) {
        LocalDateTime localDateTime = localDate;
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());

    }

    public static LocalDate getDate(String time) {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("yyyy/MM")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 30)
                .toFormatter();
        return LocalDate.parse(time, fmt);
    }
}
