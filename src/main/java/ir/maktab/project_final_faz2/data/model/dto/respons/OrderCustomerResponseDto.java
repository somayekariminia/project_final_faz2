package ir.maktab.project_final_faz2.data.model.dto.respons;

import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OrderCustomerResponseDto {
    private Long id;

    private BigDecimal offerPrice;

    private String aboutWork;

    private String startDateDoWork;

    private SubJob subJob;

    private String endDateDoWork;

    private OrderStatus orderStatus;
}
