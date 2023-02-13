package ir.maktab.project_final_faz2.data.model.dto.respons;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicJobDto {
    private Long id;

    @NotNull(message = "must not null")
    private String nameBase;
}
