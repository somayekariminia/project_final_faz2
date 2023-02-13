package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AdminRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String subService;
    private SubJob subSubject;
    double performance;
}
