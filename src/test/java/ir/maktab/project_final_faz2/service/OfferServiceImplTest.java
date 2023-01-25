package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OfferServiceImplTest {
    @Autowired
    private OfferServiceImpl offerService;
    @Autowired
    private ExpertServiceImpl expertService;
    @Autowired
    private OrderCustomerServiceImpl orderCustomerService;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("offerData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Order(2)
    @Test
    void findAllOrdersForAExpert() {
        Expert expert = expertService.findById(1L);
        List<OrderCustomer> allOrdersForAExpert = offerService.findAllOrdersForAExpert(expert);
        Assertions.assertTrue(allOrdersForAExpert.size() > 0);
    }

    @Order(1)
    @TestFactory
    Stream<DynamicTest> setOffer() {
        LocalDate start = LocalDate.of(2023, 01, 29);
        LocalDate start1 = LocalDate.of(2023, 01, 28);
        LocalDate start2 = LocalDate.of(2023, 01, 27);
        Offers[] arrayOffer = new Offers[3];
        arrayOffer[0] = Offers.builder().offerPriceByExpert(new BigDecimal(3500)).startTime(UtilDate.changeLocalDateToDate(start)).durationWork(Duration.ofHours(3)).build();
        arrayOffer[1] = Offers.builder().offerPriceByExpert(new BigDecimal(3500)).startTime(UtilDate.changeLocalDateToDate(start1)).durationWork(Duration.ofHours(3)).build();
        arrayOffer[2] = Offers.builder().offerPriceByExpert(new BigDecimal(3500)).startTime(UtilDate.changeLocalDateToDate(start2)).durationWork(Duration.ofHours(3)).build();

        List<Expert> listExpert = expertService.findAllExpertsApproved();
        for (int i = 0; i <= 2; i++) {
            arrayOffer[i].setExpert(listExpert.get(i));
        }

        return Arrays.stream(arrayOffer).map(offers -> {
            return DynamicTest.dynamicTest("save Offer", () -> {
                Assertions.assertNotNull(offerService.save(offers, "order1").getId());
            });
        });
    }

    @Order(3)
    @Test
    void viewAllOffersOrdersByCustomer() {
        List<Offers> offersForOrder = offerService.viewAllOffersOrdersByCustomer("order1");
        Assertions.assertTrue(offersForOrder.size() > 0);
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