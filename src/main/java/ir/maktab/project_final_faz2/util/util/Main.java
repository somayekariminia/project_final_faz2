package ir.maktab.project_final_faz2.util.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());

        long l = Duration.between(LocalDateTime.now(), LocalDateTime.of(2023, 02, 13, 8, 00, 00)).toHours();
        System.out.println(Math.abs(l));
        l = 0;
        Duration duration = Duration.ZERO.plusDays(1).plusHours(1);
        LocalDateTime plus = LocalDateTime.now().plus(duration);
        System.out.println(plus);


    }
}
