package ir.maktab.project_final_faz2;

import ir.maktab.project_final_faz2.service.OfferServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalTime;

@SpringBootTest
class ProjectFinalFaz2ApplicationTests {
	@BeforeAll
	static void set(){

	}
	@Autowired
  private  OfferServiceImpl offerService;
	@Test
	void contextLoads() {
	}
	@Test
	void findAllOrdersForAExpert(){
		Duration duration = Duration.between(LocalTime.NOON,LocalTime.MIDNIGHT);
		System.out.println(duration.toHoursPart());

	}

}
