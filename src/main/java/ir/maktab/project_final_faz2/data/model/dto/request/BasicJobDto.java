package ir.maktab.project_final_faz2.data.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicJobDto {
    @NotNull(message = "must not null")
    @NotBlank(message = "must not blank")
    private String nameBase;
}
