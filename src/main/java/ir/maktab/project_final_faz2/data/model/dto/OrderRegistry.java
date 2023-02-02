package ir.maktab.project_final_faz2.data.model.dto;

import lombok.Data;

@Data
public class OrderRegistry {
    private OrderCustomerDto orderCustomerDto;
    private AccountDto accountDto;
    private String nameSubJob;
}
