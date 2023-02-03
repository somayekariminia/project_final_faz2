package ir.maktab.project_final_faz2.data.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
@Getter
@Setter
public class OffersDto {
    Long Id;

    Duration durationWork;

    boolean isAccept;

    private BigDecimal offerPriceByExpert;

    private String startTime;

}
