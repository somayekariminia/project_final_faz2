package ir.maktab.project_final_faz2.data.model.dto.request;


import ir.maktab.project_final_faz2.data.model.dto.respons.OrderCustomerDto;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderRegistryDto {

    private OrderCustomerDto orderCustomerDto;

    @Email
    private String userName;

    private String nameSubJob;
}
