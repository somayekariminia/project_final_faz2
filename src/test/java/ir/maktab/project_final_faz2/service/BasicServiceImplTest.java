package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BasicServiceImplTest {
    @Autowired
    private BasicJubServiceImpl basicJubService;
    @Autowired
    private SubJobServiceImpl subJobService;
    private static BasicJob basicJob;
    private static SubJob subJob;

    @BeforeAll
    static void set() {
        basicJob = BasicJob.builder().nameBase("clean").build();
        subJob = SubJob.builder().subJobName("washing").price(new BigDecimal(2000)).description("washing sofas").build();
    }

    @Order(1)
    @Test
    void saveBasicJobTest() {
        BasicJob basicSave = basicJubService.save(basicJob);
        Assertions.assertNotNull(basicSave.getId());
    }

    @Order(2)
    @Test
    void saveSubJobTest() {
        BasicJob basicJobDb = basicJubService.findBasicJobByName("clean");
        subJob.setBasicJob(basicJobDb);
        SubJob subJobDb = subJobService.saveSubJob(subJob);
        Assertions.assertNotNull(subJobDb.getId());
    }

    @Order(3)
    @Test
    void testNotSaveBasicObject() {
        BasicJob basicJob1 = basicJob;
        Exception exception = Assertions.assertThrows(RepeatException.class, () -> basicJubService.save(basicJob1));
        Assertions.assertEquals("already basicJob "+basicJob1.getNameBase()+" is Exist"  , exception.getMessage());
    }

    @Order(4)
    @Test
    void testNotSaveSubJobWithoutBasicJob() {
        SubJob subJob1 = subJob;
        BasicJob basicJob1 = BasicJob.builder().nameBase("homeWork").build();
        subJob1.setBasicJob(basicJob1);
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> subJobService.saveSubJob(subJob1));
        Assertions.assertEquals("is not exist basicJob " + subJob.getBasicJob().getNameBase(), exception.getMessage());
    }

    @Order(5)
    @Test
    void testNotSaveSubJobExist() {
        BasicJob basicJobDb = basicJubService.findBasicJobByName("clean");
        subJob.setBasicJob(basicJobDb);
        Exception exception = Assertions.assertThrows(RepeatException.class, () -> subJobService.saveSubJob(subJob));
        Assertions.assertEquals("this subService "+  subJob.getBasicJob().getNameBase() +" Already saved", exception.getMessage());
    }

    @Order(6)
    @Test
    void findAllBasicTest() {
        Assertions.assertTrue(basicJubService.findAllBasicJobs().size() > 0);
    }

    @Order(7)
    @Test
    void findAllSubJobTest() {
        Assertions.assertTrue(subJobService.findAllSubJob().size() > 0);
    }

    @Order(8)
    @Test
    void updateSubJob() {
        SubJob newSubJob = subJob;
        newSubJob.setPrice(new BigDecimal(25000));
        newSubJob.setDescription("cleanWindows");
        SubJob subJobUpdate = subJobService.updateSubJob(newSubJob);
        Assertions.assertNotNull(subJobUpdate.getId());
        Assertions.assertEquals(subJobUpdate.getPrice(), newSubJob.getPrice());
    }

    @Order(9)
    @Test
    void findAllSubJobsABasicJobTest() {
        Assertions.assertTrue(basicJubService.findAllSubJobsABasicJob("clean").size() > 0);
    }

    @Order(10)
    @Test
    void findByNameSubJob() {
        Assertions.assertNotNull(subJobService.findSubJobByName("washing"));
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> subJobService.findSubJobByName("windows"));
        Assertions.assertEquals(String.format("Not Found %s !!!!!!!!", "windows"), exception.getMessage());
    }

    @Test
    void findByIdTest() {
       Assertions.assertNotNull(subJobService.findById(1L));
    }
    @Test
    void notFindByIdTest() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () -> subJobService.findById(2L));
        Assertions.assertEquals(String.format("Not Found %s !!!!!!!!", "2"), exception.getMessage());
    }

}
