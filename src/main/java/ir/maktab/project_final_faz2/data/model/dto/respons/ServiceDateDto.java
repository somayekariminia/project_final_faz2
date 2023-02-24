package ir.maktab.project_final_faz2.data.model.dto.respons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Setter
@Getter
@AllArgsConstructor
public class ServiceDateDto {
    private String subJob;
    private LocalDateTime date;
    private BigDecimal price;
}
