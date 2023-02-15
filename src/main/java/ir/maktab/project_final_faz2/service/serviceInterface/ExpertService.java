package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.entity.Expert;

import java.io.File;
import java.util.List;

public interface ExpertService {
    Expert save(Expert expert, File file);

    Expert login(String userName, String password);

    Expert changePassword(String userName, String passwordOld, String newPassword);

    Expert findById(Long id);

    Expert findByUserName(String userName);

    List<Expert> findAllPerson();

    List<Expert> findAllExpertsApproved();

    List<Expert> findAllExpertsIsNotConfirm();

    File viewImage(String userName, File file);
}
