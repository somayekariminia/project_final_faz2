package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.util.util.UtilImage;
import ir.maktab.project_final_faz2.util.util.ValidationInput;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpertServiceImpl {
    private final ExpertRepository expertRepository;

    public Expert save(Expert expert, File file) {
        if(expertRepository.findByEmail(expert.getEmail()).isPresent())
            throw new RepeatException("exist is expert to userName "+expert.getEmail());
        validateInfoPerson(expert);
        expert.setExpertImage(UtilImage.validateImage(file));
        expert.setSpecialtyStatus(SpecialtyStatus.NewState);
        expert.setPerformance(0);
        return expertRepository.save(expert);

    }

    private void validateInfoPerson(Expert person) {
        ValidationInput.validateName(person.getFirstName());
        ValidationInput.validateName(person.getLastName());
        ValidationInput.validateEmail(person.getEmail());
        ValidationInput.validatePassword(person.getPassword());
    }


    public Expert login(String userName, String password) {
        Expert expert = expertRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException("Expert not found with this userName " + userName));
        if (!expert.getPassword().equals(password))
            throw new ValidationException("Your password is incorrect");
        return expert;

    }


    public Expert changePassword(String userName, String passwordOld, String newPassword) {
        Expert expert = login(userName, passwordOld);
        expert.setPassword(newPassword);
        expertRepository.save(expert);
        return login(userName, newPassword);

    }

    public Expert findById(Long id) {
        return expertRepository.findById(id).orElseThrow(() -> new NotFoundException("Person not found with this userName "));
    }
    public Expert findByUserName(String userName) {
        return expertRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException("Person not found with this userName " +userName));
    }

    public List<Expert> findAllPerson() {
        List<Expert> listExpert = expertRepository.findAll();
        if (listExpert.isEmpty())
            throw new NotFoundException("there arent experts");
        return listExpert;
    }


    public List<Expert> findAllExpertsApproved() {
        return expertRepository.findAllExpertIsntConfirm(SpecialtyStatus.Confirmed);
    }


    public List<Expert> findAllExpertsIsNotConfirm() {
        return expertRepository.findAllExpertIsntConfirm(SpecialtyStatus.NewState);
    }

}
