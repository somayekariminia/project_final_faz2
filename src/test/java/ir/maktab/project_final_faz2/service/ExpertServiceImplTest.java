package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ExpertServiceImplTest {
    @Autowired
    private ExpertServiceImpl expertService;
    private static Expert expert;

    @BeforeAll
    static void set() {
        expert = Expert.builder().
                firstName("somaye").lastName("karimi").
                email("somaye@yahoo.com").password("Sok31200").build();
    }

    @Test
    void saveTest() {
        File file = new File("OIF.jpg");
        Expert expertSave = expertService.save(expert, file);
        assertNotNull(expertSave.getId());
    }

}