package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CustomerServiceImplTest {
    @Autowired
    private CustomerServiceImpl customerService;
    private static Customer customer;

    @BeforeAll
    static void set() {
        customer = Customer.builder().
                firstName("morteza").lastName("karimi").
                email("morteza@yahoo.com").password("Mok31200").build();
    }

    @Test
    void saveCustomerTest() {
        Customer customerSave = customerService.save(customer);
        assertNotNull(customerSave.getId());
    }

    @Test
    void saveDuplicateCustomer() {
        Exception exception = Assertions.assertThrows(RepeatException.class, () -> customerService.save(customer));
        Assertions.assertEquals("exist the user to the " + customer.getEmail(), exception.getMessage());
    }

    public static List<Customer> data() {
        List<Customer> arrayCustomer = List.of(
                Customer.builder().firstName("morteza").lastName("karimi").email("morteza.gmail").password("Mok31200").build(),
                Customer.builder().email("somaye@yahoo.com").password("1234vc").firstName("neda").lastName("akbari").build(),
                Customer.builder().email("neda@yahoo.com").password("1234vc").firstName("neda45").lastName("akbari123").build());
        return arrayCustomer;
    }

    @ParameterizedTest
    @MethodSource(value = {"data"})
    void dontSaveTest(Customer customer) {
        Exception exceptionEmail = Assertions.assertThrows(ValidationException.class, () -> customerService.save(customer));
        Assertions.assertEquals("Your informant Is Invalid", exceptionEmail.getMessage());
    }

    @Test
    void loginTest() {
        Customer customerExist = customerService.login(customer.getEmail(), customer.getPassword());
        assertNotNull(customerExist.getId());
    }

    @Test
    void notLoginTest() {
        Exception exceptionUserName = Assertions.assertThrows(NotFoundException.class, () -> customerService.login("sok", "12345"));
        assertEquals("customer not found with this userName", exceptionUserName.getMessage());
        Exception exceptionPassword = Assertions.assertThrows(NotFoundException.class, () -> customerService.login("morteza@yahoo.com", "12345"));
        assertEquals("Your password is incorrect", exceptionPassword.getMessage());
    }

    @Test
    void changePasswordTest() {
        Customer customerToNewPassword = customerService.changePassword(customer.getEmail(), customer.getPassword(), "Sok61058");
        assertEquals(customerToNewPassword.getPassword(), "Sok61058");
    }

}