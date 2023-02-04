package ir.maktab.project_final_faz2.data.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktab.project_final_faz2.data.model.entity.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderCustomerDto {
    private Address address;

    private Long id;

    private BigDecimal offerPrice;

    private String aboutWork;

    private String startDateDoWork;

}
