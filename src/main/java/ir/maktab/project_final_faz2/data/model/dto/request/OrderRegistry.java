package ir.maktab.project_final_faz2.data.model.dto.request;


import ir.maktab.project_final_faz2.data.model.dto.respons.OrderCustomerDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderRegistry {

   private OrderCustomerDto orderCustomerDto;

    private AccountDto accountDto;

    private String nameSubJob;
}
