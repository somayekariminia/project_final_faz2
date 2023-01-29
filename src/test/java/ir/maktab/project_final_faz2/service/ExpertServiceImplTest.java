package ir.maktab.project_final_faz2.service;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpertServiceImplTest {
    @Autowired
    private ExpertServiceImpl expertService;
    private static Expert expert;
    private static File file;

    @BeforeAll
    static void set(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("expertData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    void findAllExpertTest() {
        List<Expert> allExpert = expertService.findAllPerson();
        assertTrue(allExpert.size()>0);
    }

    @Order(8)
    @Test
    void findAllExpertsConfirmTest() {
        List<Expert> allExpertIsConfirm = expertService.findAllExpertsApproved();
        assertTrue(allExpertIsConfirm.size()>0);
    }
    @Order(8)
    @Test
    void findAllExpertsIsNotConfirmTest() {
        List<Expert> allExpertIsNotConfirm = expertService.findAllExpertsIsNotConfirm();
        assertTrue(allExpertIsNotConfirm.size()>0);
    }
    @Order(9)
    @Test
    void viewImageTest(){
        File file=new File("C:\\Users\\Lenovo\\Desktop\\PIO.jpg");
        File outPutFile = expertService.viewImage("morteza@yahoo.com", file);
        Expert expert=expertService.findByUserName("morteza@yahoo.com");
        Assertions.assertEquals(outPutFile.length()/1024,expert.getExpertImage().length/1024);
    }
}