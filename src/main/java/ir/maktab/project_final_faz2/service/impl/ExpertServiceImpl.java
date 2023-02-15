package ir.maktab.project_final_faz2.service.impl;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.enums.Role;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.exception.*;
import ir.maktab.project_final_faz2.service.interfaces.ExpertService;
import ir.maktab.project_final_faz2.util.util.UtilImage;
import ir.maktab.project_final_faz2.util.util.ValidationInput;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service

@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {
    private final ExpertRepository expertRepository;

    @Transactional
    @Override
    public Expert save(Expert expert, File file) {
        if (Objects.isNull(expert))
            throw new NullObjects("expert is null");
        if (expertRepository.findByEmail(expert.getEmail()).isPresent())
            throw new DuplicateException(String.format("already Exist is Expert %s ", expert.getEmail()));
        validateInfoPerson(expert);
        expert.setExpertImage(UtilImage.validateImage(file));
        expert.setSpecialtyStatus(SpecialtyStatus.NewState);
        expert.setPerformance(0);
        expert.setRole(Role.EXPERT);
        return expertRepository.save(expert);

    }

    private void validateInfoPerson(Expert person) {
        ValidationInput.validateName(person.getFirstName());
        ValidationInput.validateName(person.getLastName());
        ValidationInput.validateEmail(person.getEmail());
        ValidationInput.validatePassword(person.getPassword());
    }

    @Override

    public Expert login(String userName, String password) {
        Expert expert = expertRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException(String.format("Not Fount Expert %s", userName)));
        if (!expert.getPassword().equals(password))
            throw new ValidationException("Your password is incorrect");
        return expert;

    }

    @Override
    public Expert changePassword(String userName, String passwordOld, String newPassword) {
        if (passwordOld.equals(newPassword))
            throw new ValidationException("passwordNew same is old password");
        Expert expert = login(userName, passwordOld);
        expert.setPassword(newPassword);
        expertRepository.save(expert);
        Expert newExpert = findByUserName(userName);
        if (!newExpert.getPassword().equals(newPassword))
            throw new NotFoundException("Password is invalid");
        return newExpert;
    }

    @Override
    public Expert findById(Long id) {
        return expertRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Not Fount Expert %d", id)));
    }

    @Override
    public Expert findByUserName(String userName) {
        return expertRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException(String.format("Not Fount Expert %s", userName)));
    }

    @Override
    public List<Expert> findAllPerson() {
        List<Expert> listExpert = expertRepository.findAll();
        if (listExpert.isEmpty())
            throw new NotFoundException("!!!!List Experts is Null!!!!");
        return listExpert;
    }

    @Override

    public List<Expert> findAllExpertsApproved() {
        List<Expert> allExpertIsNtConfirm = expertRepository.findAllExpertIsConfirm(SpecialtyStatus.Confirmed);
        if (allExpertIsNtConfirm.isEmpty())
            throw new NotFoundException("There arent Expert Confirmed !!!!!!");
        return allExpertIsNtConfirm;
    }

    @Override
    public List<Expert> findAllExpertsIsNotConfirm() {
        List<Expert> allExpertIsNtConfirm = expertRepository.findAllExpertIsConfirm(SpecialtyStatus.NewState);
        if (allExpertIsNtConfirm.isEmpty())
            throw new NotFoundException("There arent Experts Is Not Confirm!!!!!!!");
        return allExpertIsNtConfirm;
    }

    @Override
    public File viewImage(String userName, File file) {
        Expert expert = findByUserName(userName);
        return UtilImage.getFileImage(expert.getExpertImage(), file);
    }


    public void withdrawToCreditExpert(BigDecimal amount, Expert expert) {
        Expert expertDb = findByUserName(expert.getEmail());
        if(expertDb.getSpecialtyStatus().equals(SpecialtyStatus.NewState))
            throw new NotAcceptedException(String.format("expert %s isNot confirm",expert.getEmail()));
        expertDb.getCredit().setBalance(BigDecimal.valueOf(0.7 * amount.doubleValue()));
        expertRepository.save(expertDb);
    }

    public void updateExpert(Expert expert) {
        expertRepository.save(expert);
    }

    public double findMax() {
        return expertRepository.maxPerformance();
    }
    public double findMin() {
        return expertRepository.minPerformance();
    }
}
