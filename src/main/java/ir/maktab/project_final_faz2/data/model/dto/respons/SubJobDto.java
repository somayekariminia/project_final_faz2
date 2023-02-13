package ir.maktab.project_final_faz2.data.model.dto.respons;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SubJobDto {
    private Long id;
    private BasicJob basicJob;

    @NotNull(message = "must not null")
    private String subJobName;

    private BigDecimal price;

    private String description;
}
