package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Address;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.exception.ValidationException;
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
import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest {
    @Autowired
    private OrderCustomerServiceImpl orderCustomerService;
    private static OrderCustomer orderCustomer;
    @Autowired
    private SubJobServiceImpl subJobService;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("subJobData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Date dateStartC = UtilDate.changeLocalDateToDate(LocalDate.of(2023, 01, 25));
            orderCustomer = OrderCustomer.builder().
                    offerPrice(new BigDecimal(3000))
                    .codeOrder("order1").address(Address.builder()
                            .city("kerman").street("hashtBehesht")
                            .no("512").build()).aboutWork("cleanHomeAndCooking").offerStartDateCustomer(dateStartC).build();
        }
    }

    @Order(1)
    @Test
    void saveOrderTest() {
        orderCustomer.setSubJob(subJobService.findSubJobByName("Washing"));
        orderCustomerService.saveOrder(orderCustomer);
        Assertions.assertNotNull(orderCustomer.getId());
    }

    @Order(2)
    @Test
    void testExceptionSaveDuplicateOrder() {
        Exception exception = Assertions.assertThrows(RepeatException.class, () -> orderCustomerService.saveOrder(orderCustomer));
        Assertions.assertEquals("the order is exist already to code " + orderCustomer.getCodeOrder(), exception.getMessage());
    }

    @Order(3)
    @Test
    void testExceptionValidationsSaveOrder() {
        OrderCustomer orderCustomer1 = orderCustomer;
        orderCustomer1.setCodeOrder("order2");
        orderCustomer1.setOfferStartDateCustomer(UtilDate.changeLocalDateToDate(LocalDate.of(2023,01,22)));
        Exception exceptionDate = Assertions.assertThrows(ValidationException.class, () -> orderCustomerService.saveOrder(orderCustomer1));
        Assertions.assertEquals("You can't order before today ", exceptionDate.getMessage());

    }
    @Order(4)
    @Test
    void TestDontSaveOrderByLowerPrice(){
        OrderCustomer orderCustomer1 = orderCustomer;
        orderCustomer1.setOfferPrice(new BigDecimal(1500));
        Exception exceptionPrice = Assertions.assertThrows(ValidationException.class, () -> orderCustomerService.saveOrder(orderCustomer1));
        Assertions.assertEquals("priceOffer lower of basic price", exceptionPrice.getMessage());
    }
}
