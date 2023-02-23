package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.PersonDto;
import ir.maktab.project_final_faz2.data.model.entity.Admin;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.Role;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.AdminRepository;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.data.model.repository.PersonRepository;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.mapper.MapperServices;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.serviceImpl.specification.CreateSpecificationAdmin;
import ir.maktab.project_final_faz2.service.serviceInterface.AdminService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final ExpertServiceImpl expertService;
    private final SubJobServiceImpl subJobService;
    private final ExpertRepository expertRepository;
    private final AdminRepository adminRepository;
    private final PersonRepository personRepository;
    private final MessageSourceConfiguration messageSource;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        Admin admin = new Admin();
        admin.setEmail("admin@yahoo.com");
        admin.setPassword(passwordEncoder.encode("Admin123"));
        admin.setRole(Role.ADMIN);
        adminRepository.save(admin);
    }

    private static List<PersonDto> getPersonDtos(List<Person> personList) {
        List<PersonDto> personDtoS = MapperUsers.INSTANCE.listPersonToPersonDto(personList);
        for (int i = 0; i < personList.size(); i++) {
            if (personList.get(i) instanceof Expert) {
                List<SubJob> servicesList = ((Expert) personList.get(i)).getServicesList();
                personDtoS.get(i).getSubJob().addAll(MapperServices.INSTANCE.listSubJobToSubJobDtoRes(servicesList));
                personDtoS.get(i).setPerformance(((Expert) personList.get(i)).getPerformance());
            }
        }
        return personDtoS;
    }

    @Override
    public void addExpertToSubJob(String  userName, String subJobName) {
        Expert expertDb = expertService.findByUserName(userName);
        SubJob subJob=subJobService.findSubJobByName(subJobName);
        SubJob subJobDb = subJobService.findSubJobByName(subJob.getSubJobName());
        if (!expertDb.getSpecialtyStatus().equals(SpecialtyStatus.Confirmed))
            throw new ValidationException(messageSource.getMessage("errors.message.isn't_confirm"));
        if (expertDb.getServicesList().stream().anyMatch(subJob1 -> subJob1.getSubJobName().equals(subJobDb.getSubJobName())))
            throw new DuplicateException(messageSource.getMessage("errors.message.duplicate-object"));
        expertDb.getServicesList().add(subJobDb);
        expertRepository.save(expertDb);
    }

    @Override
    public void deleteExpertOfSubJob(String userName, String subJobName) {

        Expert expertDb = expertService.findByUserName(userName);
        SubJob subJob=subJobService.findSubJobByName(subJobName);
        if (expertDb.getServicesList().stream().noneMatch(subJob1 -> subJob1.getSubJobName().equals(subJob.getSubJobName())))
            throw new NotFoundException(messageSource.getMessage("errors.message.notFound-subJob"));
        if (expertDb.getServicesList().isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        expertDb.getServicesList().remove(subJob);
        expertRepository.save(expertDb);
    }

    @Override
    public Admin changePassword(String userName, String passwordOld, String newPassword) {
        if (passwordOld.equals(newPassword))
            throw new ValidationException(messageSource.getMessage("errors.message.duplicate_password"));
        Admin admin = adminRepository.findAdminByEmail(userName).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-subJob")));
        admin.setPassword(newPassword);
        adminRepository.save(admin);
        Admin newAdmin = adminRepository.findAdminByEmail(userName).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-subJob")));
        if (!newAdmin.getPassword().equals(newPassword))
            throw new NotFoundException(messageSource.getMessage("errors.message.duplicate_password"));
        return newAdmin;
    }

    public Admin findByUserName(String userName) {
        return adminRepository.findAdminByEmail(userName).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-subJob")));
    }

    @Override
    public void isConfirmExpertByAdmin(String userName) {
        Expert expertDb = expertService.findByUserName(userName);
        if (!expertDb.getSpecialtyStatus().equals(SpecialtyStatus.WaitingForConfirm))
            throw new ValidationException(String.format("Expert To %s Username can not Confirm", userName));
        expertDb.setSpecialtyStatus(SpecialtyStatus.Confirmed);
        expertRepository.save(expertDb);
    }

    @Override
    public List<Expert> findAllExpertIsNtConFirm() {
        return expertService.findAllExpertsIsNotConfirm();
    }

    @Override
    public List<Expert> findAllIsConfirm() {
        return expertService.findAllExpertsApproved();
    }

    @Override
    public List<PersonDto> search(AdminRequestDto adminRequestDto) {
        if (adminRequestDto.getMinOrMax() != null && !adminRequestDto.getMinOrMax().isEmpty())
            maxMin(adminRequestDto);
        Specification<Person> personSpecification = CreateSpecificationAdmin.withDynamicQuery(adminRequestDto);
        List<Person> personList = personRepository.findAll(personSpecification);
        List<PersonDto> personDtoS = getPersonDtos(personList);
        if (personDtoS.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return personDtoS;
    }


    private void maxMin(AdminRequestDto adminRequestDto) {
        if (adminRequestDto.getMinOrMax().equals("max"))
            adminRequestDto.setPerformance(expertService.findMax());
        else
            adminRequestDto.setPerformance(expertService.findMin());
    }
}