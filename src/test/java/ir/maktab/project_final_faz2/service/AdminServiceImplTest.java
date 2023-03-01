package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Admin;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.service.serviceImpl.AdminServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.ExpertServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.SubJobServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminServiceImplTest {
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private ExpertServiceImpl expertService;
    @Autowired
    private SubJobServiceImpl subJobService;

    @BeforeAll
    static void setup(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("adminData.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void changePasswordTest() {
        Admin admin = adminService.findByUserName("admin@gmail.com");
        Admin adminToNewPassword = adminService.changePassword(admin.getEmail(), admin.getPassword(), "Sok61058");
        assertEquals(adminToNewPassword.getPassword(), "Sok61058");
    }

    @Order(4)
    @Test
    void addExpertToSubJob() {
        Expert expert = expertService.findById(101L);
        SubJob subJob = subJobService.findById(3333L);
        int size = expert.getServicesList().size();
        adminService.addExpertToSubJob("expert", "subJob");
        Expert expertNew = expertService.findById(101L);
        Assertions.assertEquals(size + 1, expertNew.getServicesList().size());
    }

    @Order(5)
    @Test
    void deleteExpertOfSubJob() {
        Expert expert = expertService.findById(101L);
        SubJob subJob = subJobService.findById(101L);
        int size = expert.getServicesList().size();
        adminService.deleteExpertOfSubJob("ali@yahoo.com", "101L");
        Expert expertNew = expertService.findById(101L);
        Assertions.assertEquals(size - 1, expertNew.getServicesList().size());
    }

    @Test
    @Order(2)
    void isConfirmExpertByAdmin() {
        Expert expert = expertService.findById(101L);
        adminService.isConfirmExpertByAdmin(expert.getEmail());
        Expert expertNew = expertService.findById(101L);
        Assertions.assertTrue(expertNew.getSpecialtyStatus().equals(SpecialtyStatus.Confirmed));
    }

    @Order(1)
    @Test
    void findAllExpertIsNtConFirm() {
        List<Expert> allExpertsApproved = adminService.findAllExpertIsNtConFirm();
        assertTrue(allExpertsApproved.size() > 0);
    }

    @Order(3)
    @Test
    void findAllIsConfirm() {
        List<Expert> allExpertsApproved = adminService.findAllIsConfirm();
        assertTrue(allExpertsApproved.size() > 0);
    }

}