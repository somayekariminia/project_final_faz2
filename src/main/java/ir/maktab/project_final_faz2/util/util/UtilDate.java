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
    public static Date changeLocalDateToDate(LocalDate localDate) {
        LocalDateTime localDateTime = localDate.atStartOfDay();
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());

    }

    public static LocalDateTime getLocalDateTime(Date date) {
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return ldt.toLocalDate().atStartOfDay();
    }

    public static int compareTwoDate(Date dateFirst, Date dateTwo) {
        LocalDateTime localDateFirst = getLocalDateTime(dateFirst);
        LocalDateTime localDateSecond = getLocalDateTime(dateTwo);
        return localDateFirst.compareTo(localDateSecond);
    }

    public static LocalDate getDate(String time) {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 30)
                .toFormatter();
        return LocalDate.parse(time, fmt);
    }
}
