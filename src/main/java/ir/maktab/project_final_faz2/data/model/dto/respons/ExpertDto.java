package ir.maktab.project_final_faz2.data.model.dto.respons;


import ir.maktab.project_final_faz2.data.model.entity.Credit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class ExpertDto{
    @NotNull
    @Pattern(regexp="[a-zA-Z]+", message = "Your name should be of letters and least Length 1")
    private String firstName;

    @NotNull
    @Pattern(regexp="[a-zA-Z]+", message = "Your name should be of letters and least Length 1")
    private String lastName;

    @NotNull
    @Email(message = "please your email enter correct")
    private String email;

    @NotNull
    @Pattern(regexp = "(?=.{8}$)(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9]).*$", message = "The entered password must be at least one lowercase of a capital letter and a number and be at least 8 length")
    private String password;

    private double performance;

    private Credit credit;
}
