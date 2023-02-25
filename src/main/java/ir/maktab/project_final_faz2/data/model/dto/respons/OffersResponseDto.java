package ir.maktab.project_final_faz2.data.model.dto.respons;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class OffersResponseDto {
    private BigDecimal offerPriceByExpert;

    private String startTime;

}
