package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SubJobDto {
    private BasicJob basicJob;

    @NotNull(message = "must not null")
    @NotBlank(message = "must not blank")
    private String subJobName;

    private BigDecimal price;

    private String description;
}
