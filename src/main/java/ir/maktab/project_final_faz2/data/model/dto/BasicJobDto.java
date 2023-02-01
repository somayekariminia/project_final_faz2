package ir.maktab.project_final_faz2.data.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicJobDto {
    private Long id;
    @NotNull
    private String nameBase;
}
