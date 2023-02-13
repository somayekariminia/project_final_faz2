package ir.maktab.project_final_faz2.data.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AccountDto {
    @NotNull
    @Email
    private String userName;

    @NotNull
    @Pattern(regexp = "(?=.{8}$)(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9]).*$", message = "The entered password must be at least one lowercase of a capital letter and a number and be at least 8 lenght")
    private String password;
}
