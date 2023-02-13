package ir.maktab.project_final_faz2.data.model.dto.respons;

import ir.maktab.project_final_faz2.data.model.entity.SubJob;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PersonDto {

    private String firstName;

    private String lastName;

    private String email;

    private String role;

    private double performance;

    private List<SubJob> subJob;
}
