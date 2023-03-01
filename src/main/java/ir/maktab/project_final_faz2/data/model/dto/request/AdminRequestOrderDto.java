package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AdminRequestOrderDto {

    private String lowDateStarter;
    private String lowOrBigOrEqualDate;

    private String bigDateStater;

    private String lowDateEnd;

    private String bigDateEnd;

    private String lowOrEqualOrBig;

    private BigDecimal price;

    private OrderStatus orderStatus;

    private String subService;

    private String basicService;

}
