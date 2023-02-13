package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.dto.respons.ExpertDto;
import lombok.Data;

@Data
public class ExpertAndFile {
    private ExpertDto expertDto;
    private String path;
}
