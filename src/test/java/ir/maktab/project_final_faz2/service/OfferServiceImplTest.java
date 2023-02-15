package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.service.serviceImpl.ExpertServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.OfferServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.OrderCustomerServiceImpl;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
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


    @Order(1)
    @TestFactory
    Stream<DynamicTest> setOffer() {
        LocalDate start = LocalDate.of(2023, 2, 8);
        LocalDate start1 = LocalDate.of(2023, 2, 9);
        LocalDate start2 = LocalDate.of(2023, 2, 11);
        Offers[] arrayOffer = new Offers[3];
        arrayOffer[0] = Offers.builder().offerPriceByExpert(new BigDecimal(4000)).startTime(UtilDate.changeLocalDateToDate(start)).durationWork(Duration.ZERO.plusHours(2).plusMillis(30)).build();
        arrayOffer[1] = Offers.builder().offerPriceByExpert(new BigDecimal(4500)).startTime(UtilDate.changeLocalDateToDate(start1)).durationWork(Duration.ZERO.plusHours(2).plusMillis(30)).build();
        arrayOffer[2] = Offers.builder().offerPriceByExpert(new BigDecimal(3500)).startTime(UtilDate.changeLocalDateToDate(start2)).durationWork(Duration.ZERO.plusHours(2).plusMillis(30)).build();

        List<Expert> listExpert = expertService.findAllExpertsApproved();
        for (int i = 0; i <= 2; i++) {
            arrayOffer[i].setExpert(listExpert.get(i));
        }
        return Arrays.stream(arrayOffer).map(offers -> DynamicTest.dynamicTest("save Offer", () -> {
            Assertions.assertNotNull(offerService.save(offers, 77L).getId());
        }));
    }

    @Order(4)
    @Test
    void selectAnOfferByCustomer() {
        Offers offerExist = offerService.findById(2L);
        OrderCustomer orderCustomer = orderCustomerService.findByCode("order27");
        Offers offers = offerService.selectAnOfferByCustomer(offerExist, orderCustomer);
        Assertions.assertTrue(offers.isAccept());

    }

    @Order(5)
    @Test
    void changeOrderToStartByCustomer() {
        Offers offerExist = offerService.findById(2L);
        OrderCustomer orderCustomer = orderCustomerService.findByCode("order27");
        offerService.changeOrderToStartByCustomer(offerExist, orderCustomer);
        OrderCustomer orderCustomerNew = orderCustomerService.findByCode("order27");
        Assertions.assertEquals(orderCustomerNew.getOrderStatus(), OrderStatus.Started);
    }

    @Order(6)
    @Test
    void endDoWork() {
        OrderCustomer orderCustomer = orderCustomerService.findByCode("order16");
        offerService.endDoWork(orderCustomer, LocalDateTime.now());
        OrderCustomer newOrderCustomer = orderCustomerService.findByCode("order16");
        Assertions.assertEquals(newOrderCustomer.getOrderStatus(), OrderStatus.DoItsBeen);
    }

    @Order(7)
    @Test
    void findOfferIsAccept() {
        OrderCustomer orderCustomer = orderCustomerService.findByCode("order27");
        Offers offersIsAccept = offerService.findOffersIsAccept(orderCustomer);
        Assertions.assertTrue(offersIsAccept.isAccept());
    }

    @Order(3)
    @Test
    void viewAllOffersOrdersByCustomerOrderByPriceTest() {
        List<Offers> offersForOrder = offerService.viewAllOffersOrderByPriceAsc(77L);
        Assertions.assertTrue(offersForOrder.size() > 0);
        Comparator<Offers> comparator = Comparator.comparing(Offers::getOfferPriceByExpert);
        Assertions.assertTrue(comparator.compare(offersForOrder.get(0), offersForOrder.get(1)) <= 0);
    }

    @Order(3)
    @Test
    void viewAllOffersOrderByPriceDesc() {
        List<Offers> offers = offerService.viewAllOffersOrderByPriceDesc(77L);
        Comparator<Offers> comparator = Comparator.comparing(Offers::getOfferPriceByExpert);
        Assertions.assertTrue(comparator.compare(offers.get(0), offers.get(1)) >= 0);
    }

    @Order(3)
    @Test
    void viewAllOffersOrdersByCustomerOrderByPerformanceExpertTest() {
        List<Offers> offersForOrder = offerService.viewAllOrdersOrderByScoreExpertAsc(77L);
        Assertions.assertTrue(offersForOrder.size() > 0);
        Comparator<Offers> comparator = Comparator.comparing(offers -> offers.getExpert().getPerformance());
        Assertions.assertTrue(comparator.compare(offersForOrder.get(0), offersForOrder.get(1)) <= 0);
    }

    @Order(3)
    @Test
    void viewAllOrdersOrderByScoreExpertDesc() {
        List<Offers> offersOrderDesc = offerService.viewAllOrdersOrderByScoreExpertDesc(77L);
        Comparator<Offers> comparator = Comparator.comparing(offers -> offers.getExpert().getPerformance());
        Assertions.assertTrue(comparator.compare(offersOrderDesc.get(0), offersOrderDesc.get(1)) >= 0);
    }

    @Order(4)
    @Test
    void findAllOrdersForAnSubJobOfExpert() {
        Expert expert = expertService.findById(1111L);
        List<OrderCustomer> allOrdersForAExpert = offerService.findAllOrdersForAnSubJobOfExpert(expert, expert.getServicesList().get(1));
        Assertions.assertTrue(allOrdersForAExpert.size() > 0);
    }


    @Test
    void findAllSubJubExpert() {
        Expert expert = expertService.findById(1111L);
        Assertions.assertTrue(offerService.findAllSubJubExpert(expert).size() > 0);
    }
}