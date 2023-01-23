package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void dontSaveTest(){

    }
}