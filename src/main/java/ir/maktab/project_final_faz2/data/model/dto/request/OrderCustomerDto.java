package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.entity.Address;
import lombok.Getter;
import lombok.Setter;

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
