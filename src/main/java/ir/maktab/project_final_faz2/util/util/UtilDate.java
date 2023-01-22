package ir.maktab.project_final_faz2.util.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class UtilDate {
    public static Date changeLocalDateToDate(LocalDate localDate) {
        LocalDateTime localDateTime = localDate.atStartOfDay();
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());

    }

    public static LocalDate getLocalDateTime(Date date) {
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return ldt.toLocalDate();
    }

    public static int compareTwoDate(Date dateFirst, Date dateTwo) {
        LocalDate localDateFirst = getLocalDateTime(dateFirst);
        LocalDate localDateSecond = getLocalDateTime(dateTwo);
        return localDateFirst.compareTo(localDateSecond);
    }
}
