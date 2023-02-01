package ir.maktab.project_final_faz2.data.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ir.maktab.project_final_faz2.data.model.entity.Address;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
public class OrderCustomerDto {
    private Long id;

    Address address;

    private BigDecimal offerPrice;

    private String aboutWork;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date StartDateDoWork;

    private SubJob subJob;
}
