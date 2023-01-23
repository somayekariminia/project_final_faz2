package ir.maktab.project_final_faz2.service;

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
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class OfferServiceImplTest {
 @Autowired
    static  OfferServiceImpl offerService;

    @BeforeAll
    public static void set() {
        Customer customer = Customer.builder().firstName("somaye").lastName("karimi").email("somaye@yahoo.com").password("Sok31200").build();
        Expert expert = Expert.builder().firstName("morteza").lastName("karimi").email("morteza@yahoo.com").password("Mok31200").build();
        SubJob subJob = SubJob.builder().subJobName("sofa").price(new BigDecimal(2000)).description("washing").build();
    }

    @Test
    void findAllOrdersForAExpert() {
        Expert expert=null;
        List<OrderCustomer> allOrdersForAExpert = offerService.findAllOrdersForAExpert(expert);
    }

    @Test
    void save() {

    }

    @Test
    void viewAllOffersOrdersByCustomer() {
    }

    @Test
    void selectAnOfferByCustomer() {
    }

    @Test
    void updateStatusOrder() {
    }

    @Test
    void changeOrderToStartByCustomer() {
    }

    @Test
    void endDoWork() {
    }
}