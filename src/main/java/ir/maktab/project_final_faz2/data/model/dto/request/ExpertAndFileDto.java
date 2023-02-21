package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.dto.respons.ExpertDto;
import ir.maktab.project_final_faz2.data.model.entity.Credit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ExpertAndFileDto {
    private ExpertDto expertDto;
    private MultipartFile file;
}
