package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Address;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.TimeOutException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.service.serviceImpl.CustomerServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.OrderCustomerServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.SubJobServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest {
    private static OrderCustomer orderCustomer;
    @Autowired
    private OrderCustomerServiceImpl orderCustomerService;
    @Autowired
    private SubJobServiceImpl subJobService;
    @Autowired
    private CustomerServiceImpl customerService;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("subJobData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            LocalDateTime localDateTime = (LocalDateTime.of(2023, 2, 9, 0, 0, 0));
            orderCustomer = OrderCustomer.builder().
                    offerPrice(new BigDecimal(3000))
                    .address(Address.builder()
                            .city("kerman").street("hashtBehesht")
                            .noHouse("512").build()).aboutWork("cleanHomeAndCooking").startDateDoWork(localDateTime).build();
        }
    }

    @Order(1)
    @Test
    void saveOrderTest() {
        orderCustomer.setSubJob(subJobService.findSubJobByName("Washing"));
        orderCustomer.setCustomer(customerService.findByUserName("tara@gmail.com"));
        orderCustomerService.save(orderCustomer);
        Assertions.assertNotNull(orderCustomer.getId());
    }

    @Order(2)
    @Test
    void testExceptionSaveDuplicateOrder() {
        Exception exception = Assertions.assertThrows(DuplicateException.class, () -> orderCustomerService.save(orderCustomer));
        Assertions.assertEquals(String.format("the order is exist already to code: %s"), exception.getMessage());
    }

    @Order(3)
    @Test
    void testExceptionValidationsSaveOrder() {
        OrderCustomer orderCustomer1 = orderCustomer;
        orderCustomer1.setStartDateDoWork(LocalDateTime.of(2023, 1, 22, 0, 0, 0));
        Exception exceptionDate = Assertions.assertThrows(TimeOutException.class, () -> orderCustomerService.save(orderCustomer1));
        Assertions.assertEquals("The current date is less than the proposed date", exceptionDate.getMessage());

    }

    @Order(4)
    @Test
    void TestDontSaveOrderByLowerPrice() {
        OrderCustomer orderCustomer1 = orderCustomer;
        orderCustomer1.setOfferPrice(new BigDecimal(1500));
        Exception exceptionPrice = Assertions.assertThrows(ValidationException.class, () -> orderCustomerService.save(orderCustomer1));
        Assertions.assertEquals(String.format("The offer price by Customer for this sub-service %s is lower than the original price", orderCustomer1.getSubJob().getSubJobName()), exceptionPrice.getMessage());
    }

    @Order(5)
    @Test
    void findAllOrdersBySubJobTest() {
        SubJob subJob = subJobService.findById(33333L);
        Assertions.assertTrue(orderCustomerService.findAllOrdersBySubJob(subJob).size() > 0);
    }

    @Order(5)
    @Test
    void notFindAllOrdersBySubJobTest() {
        SubJob subJob = subJobService.findById(333333L);
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> orderCustomerService.findAllOrdersBySubJob(subJob));
        Assertions.assertEquals(String.format("!!!No Order for This SubJob %s", subJob.getSubJobName()), exception.getMessage());
    }

    @Order(6)
    @Test
    void findOrderById() {
        OrderCustomer orderCustomerDb = orderCustomerService.findById(1L);
        Assertions.assertNotNull(orderCustomerDb.getId());
    }

    @Order(6)
    @Test
    void findOrderByOrderCode() {
        OrderCustomer orderCustomerDb = orderCustomerService.findById(77L);
        Assertions.assertNotNull(orderCustomerDb.getId());
    }

    @Order(7)
    @Test
    void notFindOrderByOrderCode() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> orderCustomerService.findById(7L));
        Assertions.assertEquals(String.format("there arent any orderCustomer to code %s ", "order6"), exception.getMessage());
    }

    @Order(10)
    @Test
    void findOrdersCustomer() {
        List<OrderCustomer> listOrdersCustomer = orderCustomerService.findOrdersCustomer("tara@gmail.com", OrderStatus.WaitingSelectTheExpert);
        Assertions.assertTrue(listOrdersCustomer.size() > 0);
    }

    @Order(11)
    @Test
    void notFoundOrdersCustomer() {
        Customer customer = customerService.findByUserName("shams@gmail.com");
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> orderCustomerService.findOrdersCustomer("shams@gmail.com", OrderStatus.WaitingSelectTheExpert));
        Assertions.assertEquals(String.format("there aren't order for this Customer %s", customer.getEmail()), exception.getMessage());
    }
}
