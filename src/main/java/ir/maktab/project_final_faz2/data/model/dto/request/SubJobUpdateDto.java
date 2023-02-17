package ir.maktab.project_final_faz2.data.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SubJobUpdateDto {
    @NotNull
    @NotBlank
    private String subJobName;
    private BigDecimal price;
    private String description;
}
