package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceImplTest {
    private static Customer customer;
    @Autowired
    private CustomerServiceImpl customerService;

    @BeforeAll
    static void setup() {
        customer = Customer.builder().
                firstName("morteza").lastName("karimi").
                email("morteza@yahoo.com").password("Mok31200").build();
    }

    public static List<Customer> data() {
        List<Customer> arrayCustomer = List.of(
                Customer.builder().firstName("morteza").lastName("karimi").email("morteza.gmail").password("Mok31200").build(),
                Customer.builder().email("somaye@yahoo.com").password("1234vc").firstName("neda").lastName("akbari").build(),
                Customer.builder().email("neda@yahoo.com").password("1234vc").firstName("neda45").lastName("akbari123").build());
        return arrayCustomer;
    }

    @Order(1)
    @Test
    void saveCustomerTest() {
        Customer customerSave = customerService.save(customer);
        assertNotNull(customerSave.getId());
    }

    @Order(2)
    @Test
    void saveDuplicateCustomer() {
        Exception exception = Assertions.assertThrows(DuplicateException.class, () -> customerService.save(customer));
        Assertions.assertEquals("exist the user " + customer.getEmail(), exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource(value = {"data"})
    void dontSaveTest(Customer customer) {
        Exception exceptionEmail = Assertions.assertThrows(ValidationException.class, () -> customerService.save(customer));
        Assertions.assertEquals("Your informant Is Invalid", exceptionEmail.getMessage());
    }

    @Order(3)
    @Test
    void loginTest() {
        Customer customerExist = customerService.login(customer.getEmail(), customer.getPassword());
        assertNotNull(customerExist.getId());
    }

    @Order(4)
    @Test
    void notLoginTest() {
        Exception exceptionUserName = Assertions.assertThrows(NotFoundException.class, () -> customerService.login("sok", "12345"));
        assertEquals("sok" + " Not Found !!!", exceptionUserName.getMessage());
        Exception exceptionPassword = Assertions.assertThrows(ValidationException.class, () -> customerService.login("morteza@yahoo.com", "12345"));
        assertEquals("Your password is incorrect", exceptionPassword.getMessage());
    }

    @Order(5)
    @Test
    void changePasswordTest() {
        Customer customerToNewPassword = customerService.changePassword(customer.getEmail(), customer.getPassword(), "Sok61058");
        assertEquals(customerToNewPassword.getPassword(), "Sok61058");
    }

    @Order(6)
    @Test
    void findCustomerByUserName() {
        Customer customer1 = customerService.findByUserName("morteza@yahoo.com");
        Assertions.assertNotNull(customer1.getId());
    }

    @Test
    void notFindCustomerByUserName() {
        Exception exceptionPassword = Assertions.assertThrows(NotFoundException.class, () -> customerService.findByUserName("somaye@yahoo.com"));
        assertEquals("somaye@yahoo.com" + " Not Found !!!", exceptionPassword.getMessage());
    }
}