package ir.maktab.project_final_faz2.data.model.dto.respons;


import ir.maktab.project_final_faz2.data.model.entity.Credit;
import ir.maktab.project_final_faz2.data.model.entity.Review;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Data
public class ExpertDto{
    private String firstName;

    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "(?=.{8}$)(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9]).*$", message = "The entered password must be at least one lowercase of a capital letter and a number and be at least 8 lenght")
    private String password;

    private double performance;

    private Credit credit;
}
