package ir.maktab.project_final_faz2.data.model.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[0-9]{16}")
    String numberCard;
    @NotNull
    @Pattern(regexp = "^[0-9]{3,4}")
    String cvv2;
    Date expiredDate;
    private BigDecimal balance;
}
