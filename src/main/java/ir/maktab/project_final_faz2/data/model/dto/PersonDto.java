package ir.maktab.project_final_faz2.data.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ir.maktab.project_final_faz2.data.model.entity.Credit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExpertDto.class, name = "expert"),
        @JsonSubTypes.Type(value = CustomerDto.class, name = "customer")
})
public class PersonDto {
    private Long id;

    private String firstName;

    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp="(?=.{8}$)(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9]).*$",message = "The entered password must be at least one lowercase of a capital letter and a number and be at least 8 lenght")
    private String password;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date RegistrationDate;

    private Credit credit;
}
