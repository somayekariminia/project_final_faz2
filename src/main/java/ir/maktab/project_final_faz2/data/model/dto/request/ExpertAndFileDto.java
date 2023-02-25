package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.dto.respons.ExpertDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ExpertAndFileDto {
    private ExpertDto expertDto;
    private MultipartFile file;
}
