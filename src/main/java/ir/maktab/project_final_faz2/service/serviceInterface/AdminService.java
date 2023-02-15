package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.PersonDto;
import ir.maktab.project_final_faz2.data.model.entity.Admin;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;

import java.util.List;

public interface AdminService {
    void addExpertToSubJob(Expert expert, SubJob subJob);

    void deleteExpertOfSubJob(Expert expert, SubJob subJob);

    void isConfirmExpertByAdmin(String userName);

    List<Expert> findAllExpertIsNtConFirm();

    List<PersonDto> search(AdminRequestDto adminRequestDto);

    List<Expert> findAllIsConfirm();

    Admin changePassword(String userName, String passwordOld, String newPassword);
}
