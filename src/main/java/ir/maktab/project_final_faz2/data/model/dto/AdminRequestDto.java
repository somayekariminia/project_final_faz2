package ir.maktab.project_final_faz2.data.model.dto;

import ir.maktab.project_final_faz2.data.model.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminRequestDto {
   private  String name;
   private String lastName;
   @Email
   private String email;
   private String role;
   double performanceExpert;

}
