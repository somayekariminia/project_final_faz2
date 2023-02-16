package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.enums.Role;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.exception.*;
import ir.maktab.project_final_faz2.service.serviceInterface.ExpertService;
import ir.maktab.project_final_faz2.util.util.UtilImage;
import ir.maktab.project_final_faz2.util.util.ValidationInput;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service

@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {
    private final ExpertRepository expertRepository;
    @Autowired
    MessageSourceConfiguration messageSource;

    @Transactional
    @Override
    public Expert save(Expert expert, File file) {
        if (Objects.isNull(expert))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        if (expertRepository.findByEmail(expert.getEmail()).isPresent())
            throw new DuplicateException(messageSource.getMessage("errors.message.duplicate-object"));
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
        Expert expert = expertRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-object")));
        if (!expert.getPassword().equals(password))
            throw new ValidationException(messageSource.getMessage("errors.message.invalid_password"));
        return expert;

    }

    @Override
    public Expert changePassword(String userName, String passwordOld, String newPassword) {
        if (passwordOld.equals(newPassword))
            throw new ValidationException(messageSource.getMessage("errors.message.duplicate_password"));
        Expert expert = login(userName, passwordOld);
        expert.setPassword(newPassword);
        expertRepository.save(expert);
        Expert newExpert = findByUserName(userName);
        if (!newExpert.getPassword().equals(newPassword))
            throw new NotFoundException(messageSource.getMessage("errors.message.invalid_password"));
        return newExpert;
    }

    @Override
    public Expert findById(Long id) {
        return expertRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-object")));
    }

    @Override
    public Expert findByUserName(String userName) {
        return expertRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-object")));
    }

    @Override
    public List<Expert> findAllPerson() {
        List<Expert> listExpert = expertRepository.findAll();
        if (listExpert.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return listExpert;
    }

    @Override

    public List<Expert> findAllExpertsApproved() {
        List<Expert> allExpertIsNtConfirm = expertRepository.findAllExpertIsConfirm(SpecialtyStatus.Confirmed);
        if (allExpertIsNtConfirm.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return allExpertIsNtConfirm;
    }

    @Override
    public List<Expert> findAllExpertsIsNotConfirm() {
        List<Expert> allExpertIsNtConfirm = expertRepository.findAllExpertIsConfirm(SpecialtyStatus.NewState);
        if (allExpertIsNtConfirm.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return allExpertIsNtConfirm;
    }

    @Override
    public File viewImage(String userName, File file) {
        Expert expert = findByUserName(userName);
        return UtilImage.getFileImage(expert.getExpertImage(), file);
    }


    public void withdrawToCreditExpert(BigDecimal amount, Expert expert) {
        Expert expertDb = findByUserName(expert.getEmail());
        if (expertDb.getSpecialtyStatus().equals(SpecialtyStatus.NewState))
            throw new NotAcceptedException(messageSource.getMessage("errors.message.isn't_confirm"));
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
