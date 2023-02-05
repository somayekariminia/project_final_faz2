package ir.maktab.project_final_faz2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = "ir.maktab.project_final_faz2")
@ServletComponentScan
public class ProjectFinalFaz2Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectFinalFaz2Application.class, args);
    }

}
