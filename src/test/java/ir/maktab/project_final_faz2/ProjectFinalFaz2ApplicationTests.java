package ir.maktab.project_final_faz2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;


@SpringBootTest
class ProjectFinalFaz2ApplicationTests {
 @Test
    public void diff(){
     Duration duration=Duration.between(LocalDateTime.of(2023,02,5,9,30,0),LocalDateTime.of(2023,02,5,12,30,0));
     System.out.println(duration.toHours());
 }
}
