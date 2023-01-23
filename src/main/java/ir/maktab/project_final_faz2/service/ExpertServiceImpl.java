package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.util.util.UtilImage;
import ir.maktab.project_final_faz2.util.util.ValidationInput;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpertServiceImpl {
   private final ExpertRepository expertRepository;
   public Expert save(Expert expert, File file) {
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

}
