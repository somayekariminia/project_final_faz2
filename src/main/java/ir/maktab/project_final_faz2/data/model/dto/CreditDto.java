package ir.maktab.project_final_faz2.data.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreditDto {
    @NotNull
    private Long id;

    @NotNull
    String numberCard;
    @Min(3)
    @Max(4)
    @NotNull
    String cvv2;

    @JsonFormat(pattern="yyyy-MM-dd")
    Date expiredDate;

    private BigDecimal balance;
}
