package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpertServiceImplTest {
    @Autowired
    private ExpertServiceImpl expertService;
    private static Expert expert;
    private static File file;

    @BeforeAll
    static void set() {
        expert = Expert.builder().
                firstName("somaye").lastName("karimi").
                email("somaye@yahoo.com").password("Sok31200").build();
        file = new File("OIF.jpg");
    }

    static Expert[] dataExpert() {
        Expert[] array = new Expert[3];
        array[0] = Expert.builder().firstName("somaye").lastName("karimi").email("somaye@yahoo.com").password("Sok31200").build();
        array[1] = Expert.builder().firstName("morteza").lastName("karimi").email("morteza@yahoo.com").password("Mok31200").build();
        array[2] = Expert.builder().firstName("Ali").lastName("ahmadi").email("ali@yahoo.com").password("Aoh31200").build();
        return array;
    }

    @ParameterizedTest
    @MethodSource(value = "dataExpert")
    void saveTest(Expert expert) {
        Expert expertSave = expertService.save(expert, file);
        assertNotNull(expertSave.getId());
    }

    @Order(2)
    @Test
    void saveDuplicateExpert() {
        Exception exception = Assertions.assertThrows(RepeatException.class, () -> expertService.save(expert, file));
        Assertions.assertEquals("exist is expert to userName "+expert.getEmail(), exception.getMessage());
    }

    public static List<Expert> data() {
        List<Expert> arrayOfExpert = List.of(
                Expert.builder().firstName("morteza").lastName("karimi").email("morteza.gmail").password("Mok31200").build(),
                Expert.builder().email("somaye123@yahoo.com").password("1234vc").firstName("neda").lastName("akbari").build(),
                Expert.builder().email("neda@yahoo.com").password("1234vc").firstName("neda45").lastName("akbari123").build());
        return arrayOfExpert;
    }

    @ParameterizedTest
    @MethodSource(value = {"data"})
    void dontSaveTest(Expert expert) {
        Exception exceptionEmail = Assertions.assertThrows(ValidationException.class, () -> expertService.save(expert, file));
        Assertions.assertEquals("Your informant Is Invalid", exceptionEmail.getMessage());
    }

    @Order(3)
    @Test
    void loginTest() {
        Expert customerExist = expertService.login(expert.getEmail(), expert.getPassword());
        assertNotNull(customerExist.getId());
    }

    @Order(4)
    @Test
    void notLoginTest() {
        Exception exceptionUserName = Assertions.assertThrows(NotFoundException.class, () -> expertService.login("sok", "12345"));
        assertEquals("Expert not found with this userName " +"sok" , exceptionUserName.getMessage());
        Exception exceptionPassword = Assertions.assertThrows(ValidationException.class, () -> expertService.login("morteza@yahoo.com", "12345"));
        assertEquals("Your password is incorrect", exceptionPassword.getMessage());
    }

    @Order(5)
    @Test
    void changePasswordTest() {
        Expert expertToNewPassword = expertService.changePassword(expert.getEmail(), expert.getPassword(), "Sok61058");
        assertEquals(expertToNewPassword.getPassword(), "Sok61058");
    }

    @Order(6)
    @Test
    void findCustomerByUserName() {
        Expert expert1 = expertService.findByUserName("morteza@yahoo.com");
        Assertions.assertNotNull(expert1.getId());
    }

    @Order(7)
    @Test
    void notFindCustomerByUserName() {
        Exception exceptionPassword = Assertions.assertThrows(NotFoundException.class, () -> expertService.findByUserName("salman@yahoo.com"));
        assertEquals("Person not found with this userName " + "salman@yahoo.com", exceptionPassword.getMessage());
    }

    @Order(8)
    @Test
    void findAllExpertsApprovedTest() {
        List<Expert> allExpertsApproved = expertService.findAllExpertsApproved();
        assertTrue(allExpertsApproved.size()>0);

    }
    @Test
    void findAllExpertsIsNotConfirmTest() {
        List<Expert> allExpertIsNotConfirm = expertService.findAllExpertsIsNotConfirm();
        assertTrue(allExpertIsNotConfirm.size()>0);
    }
    @Order(9)
    @Test
    void viewImageTest(){
        Expert expert1 = expertService.findByUserName("morteza@yahoo.com");
        expertService.viewImage(expert1);
    }
}