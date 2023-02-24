package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.dto.respons.ExpertDto;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import jakarta.mail.MessagingException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ExpertService {
    Expert save(ExpertDto expertDto) throws MessagingException, IOException;

    Expert login(String userName, String password);

    Expert changePassword(String userName, String passwordOld, String newPassword);

    Expert findById(Long id);

    Expert findByUserName(String userName);

    List<Expert> findAllPerson();

    List<Expert> findAllExpertsApproved();

    List<Expert> findAllExpertsIsNotConfirm();

    File viewImage(String userName, File file);
}
