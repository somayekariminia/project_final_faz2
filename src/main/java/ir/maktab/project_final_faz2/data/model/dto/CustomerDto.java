package ir.maktab.project_final_faz2.data.model.dto;

import ir.maktab.project_final_faz2.data.model.entity.Credit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerDto{

    private Credit credit;

    private String firstName;

    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp="(?=.{8}$)(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9]).*$",message = "The entered password must be at least one lowercase of a capital letter and a number and be at least 8 lenght")
    private String password;


}
