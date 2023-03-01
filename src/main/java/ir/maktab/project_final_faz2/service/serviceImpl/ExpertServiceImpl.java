package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.dto.respons.ExpertDto;
import ir.maktab.project_final_faz2.data.model.entity.Credit;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.enums.Role;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.NotAcceptedException;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.serviceInterface.ExpertService;
import ir.maktab.project_final_faz2.util.util.UtilImage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpertServiceImpl implements ExpertService {
    private final ExpertRepository expertRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final MessageSourceConfiguration messageSource;
    private final JavaMailSender mailSender;

    @Transactional
    @Override
    public Expert save(ExpertDto expertDto) throws MessagingException, IOException {
        Expert expert = MapperUsers.INSTANCE.expertDtoToExpert(expertDto);
        if (expertRepository.findByEmail(expert.getEmail()).isPresent())
            throw new DuplicateException(messageSource.getMessage("errors.message.duplicate-object"));
        expert.setExpertImage(UtilImage.validationImage(expertDto.getMultipartFile().getBytes()));
        Credit credit = Credit.builder().balance(new BigDecimal("0.0")).build();
        expert.setCredit(credit);
        expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        expert.setSpecialtyStatus(SpecialtyStatus.NewState);
        expert.setPerformance(0);
        expert.setRole(Role.EXPERT);
        String randomCode = RandomString.make(20);
        expert.setCodeValidate(randomCode);
        Expert expertSave = expertRepository.save(expert);
        sendVerificationEmail(expert);
        return expertSave;
    }

    public Expert save(Expert expert) {
        return expertRepository.save(expert);
    }

    private void sendVerificationEmail(Expert expert) throws MessagingException, UnsupportedEncodingException {
        String siteURL = "http://localhost:6565/expert";
        String toAddress = expert.getEmail();
        String fromAddress = "somayekariminia6868@gmail.com";
        String senderName = "team maktab";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "team maktab.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", expert.getFirstName());
        String verifyURL = siteURL + "/verify?code=" + expert.getCodeValidate();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
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

    public Expert verify(String verificationCode) {
        Expert expert = expertRepository.findByCodeValidate(verificationCode).orElseThrow(() -> new NotFoundException("This email has already been registry"));
        expert.setCodeValidate(null);
        expert.setSpecialtyStatus(SpecialtyStatus.WaitingForConfirm);
        expert.setEnable(true);
        expertRepository.save(expert);
        return expert;
    }

}
