package ir.maktab.project_final_faz2;

import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.data.model.repository.CustomerRepository;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.data.model.repository.OrderCustomerRepository;
import ir.maktab.project_final_faz2.data.model.repository.SubJobRepository;
import ir.maktab.project_final_faz2.service.OfferServiceImpl;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;


@SpringBootTest
@DataJpaTest
@ExtendWith(SpringExtension.class)
class ProjectFinalFaz2ApplicationTests {
    @Autowired
    static OrderCustomerRepository orderCustomerRepository;
    @Autowired
    static CustomerRepository customerRepository;
    @Autowired
    static ExpertRepository expertRepository;
    @Autowired
    static SubJobRepository subJobRepository;
    @Autowired
    private static OfferServiceImpl offerService;

    @BeforeAll
    static void set() {
        Customer customer = Customer.builder().firstName("somaye").lastName("karimi").email("somaye@yahoo.com").password("Sok31200").build();
        Expert expert = Expert.builder().firstName("morteza").lastName("karimi").email("morteza@yahoo.com").password("Mok31200").build();
        SubJob subJob = SubJob.builder().subJobName("sofa").price(new BigDecimal(2000)).description("washing").build();
        expertRepository.save(expert);
        customerRepository.save(customer);
        subJobRepository.save(subJob);
        LocalDate start = LocalDate.of(2023, 01, 27);
        OrderCustomer orderCustomer = OrderCustomer.
                builder().codeOrder("order1").
                offerStartDateCustomer(UtilDate.changeLocalDateToDate(start)).
                aboutWork("clean thehome").address(Address.builder().
                        city("kerman").build()).subJob(subJob).offerPrice(new BigDecimal(3000)).build();
        orderCustomerRepository.save(orderCustomer);
        LocalDateTime startExpert = LocalDateTime.of(2023, 01, 28, 8, 0, 0);
        Offers offers = Offers.builder().expert(expert).offerPriceByExpert(new BigDecimal(3200)).durationWork(Duration.ofHours(8)).startTime(UtilDate.changeLocalDateToDate(LocalDate.from(startExpert))).build();
        offerService.save(offers, orderCustomer);
    }

    @Test
    void contextLoads() {
    }

}
