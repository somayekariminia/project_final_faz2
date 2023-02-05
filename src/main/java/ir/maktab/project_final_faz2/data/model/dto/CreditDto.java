package ir.maktab.project_final_faz2.data.model.dto;



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
    @Pattern(regexp = "^[0-9]{16}",message = "numberCard must number and length 16 !!!")
    String numberCard;
    @NotNull
    @Pattern(regexp = "^[0-9]{3,4}",message = "numberCard must number and length 3 or 4 !!!")
    String cvv2;
    String expiredDate;

    private BigDecimal balance;

    private String captcha;

    private String userName;
}
