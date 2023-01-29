package ir.maktab.project_final_faz2.service.interfaces;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;

import java.util.List;

public interface AdminService {
    void addExpertToSubJob(Expert expert, SubJob subJob);

    void deleteExpertOfSubJob(Expert expert, SubJob subJob);

    void isConfirmExpertByAdmin(String userName);

    List<Expert> findAllExpertIsNtConFirm();

    List<Expert> findAllIsConfirm();
}
