package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminRequestDto {
    private String firstName;

    private String lastName;

    @Email
    private String email;

    private Role role;

    private String subService;

    private SubJob subSubject;

    private String minOrMax;

    private String registrationDate;

    private String registrationDateTwo;

    private String numberOrderCustomer;

    private String lowOrBigOrEqual;

    private String numberOrderDone;

    private double performance;

}
