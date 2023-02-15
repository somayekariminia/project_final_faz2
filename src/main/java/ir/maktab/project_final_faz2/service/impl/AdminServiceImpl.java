package ir.maktab.project_final_faz2.service.impl;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.PersonDto;
import ir.maktab.project_final_faz2.data.model.entity.Admin;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.AdminRepository;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.data.model.repository.PersonRepository;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.mapper.MapperServices;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.interfaces.AdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final ExpertServiceImpl expertService;

    private final SubJobServiceImpl subJobService;

    private final ExpertRepository expertRepository;
    private final AdminRepository adminRepository;
    private final PersonRepository personRepository;


    public AdminServiceImpl(ExpertServiceImpl expertService, SubJobServiceImpl subJobService, ExpertRepository expertRepository,
                            AdminRepository adminRepository, PersonRepository personRepository) {
        this.expertService = expertService;
        this.subJobService = subJobService;
        this.expertRepository = expertRepository;
        this.adminRepository = adminRepository;
        this.personRepository = personRepository;
    }

    @Override
    public void addExpertToSubJob(Expert expert, SubJob subJob) {
        Expert expertDb = expertService.findByUserName(expert.getEmail());
        SubJob subJobDb = subJobService.findSubJobByName(subJob.getSubJobName());
        if (expertDb.getSpecialtyStatus().equals(SpecialtyStatus.NewState))
            throw new ValidationException(String.format("the Expert %s isNot confirm ", expertDb.getEmail()));
        if (expertDb.getServicesList().stream().anyMatch(subJob1 -> subJob1.getSubJobName().equals(subJobDb.getSubJobName())))
            throw new DuplicateException(String.format("%s already exist ", subJob.getSubJobName()));
        expertDb.getServicesList().add(subJobDb);
        expertRepository.save(expertDb);
    }

    @Override
    public void deleteExpertOfSubJob(Expert expert, SubJob subJob) {
        Expert expertDb = expertService.findByUserName(expert.getEmail());
        if (expertDb.getServicesList().stream().noneMatch(subJob1 -> subJob1.getSubJobName().equals(subJob.getSubJobName())))
            throw new NotFoundException(String.format("there arent subJob for the Expert %s !!! ", expert.getEmail()));
        if (expertDb.getServicesList().isEmpty())
            throw new NotFoundException(String.format("list subJobs Expert %s is Null !!!", expertDb.getEmail()));
        expertDb.getServicesList().remove(subJob);
        expertRepository.save(expertDb);
    }

    @Override
    public Admin changePassword(String userName, String passwordOld, String newPassword) {
        if (passwordOld.equals(newPassword))
            throw new ValidationException("passwordNew same is old password");
        Admin admin = adminRepository.findAdminByUserName(userName).orElseThrow(() -> new NotFoundException(String.format("Not fount username %s", userName)));
        admin.setPassword(newPassword);
        adminRepository.save(admin);
        Admin newAdmin = adminRepository.findAdminByUserName(userName).orElseThrow(() -> new NotFoundException(String.format("Not fount username %s", userName)));
        if (!newAdmin.getPassword().equals(newPassword))
            throw new NotFoundException("Password is invalid!!!");
        return newAdmin;
    }

    public Admin findByUserName(String userName) {
        return adminRepository.findAdminByUserName(userName).orElseThrow(() -> new NotFoundException(String.format("Not fount username %s", userName)));
    }

    @Override
    public void isConfirmExpertByAdmin(String userName) {
        Expert expertDb = expertService.findByUserName(userName);
        if (expertDb.getSpecialtyStatus().equals(SpecialtyStatus.Confirmed))
            throw new ValidationException(String.format("Expert To %s Username Is Confirm", userName));
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

    public List<PersonDto> findAllPerson(AdminRequestDto adminRequestDto) {
        if (adminRequestDto.getSubService() != null && !adminRequestDto.getSubService().isEmpty())
            subService(adminRequestDto);
        if (adminRequestDto.getTypePerformance() != null && !adminRequestDto.getTypePerformance().isEmpty())
            maxMin(adminRequestDto);
        Specification<Person> personSpecification = PersonRepository.withDynamicQuery(adminRequestDto);
        List<Person> personList = personRepository.findAll(personSpecification);
        List<PersonDto> personDtoS = MapperUsers.INSTANCE.listPersonToPersonDto(personList);
        for (int i = 0; i < personList.size(); i++) {
            if (personList.get(i) instanceof Expert) {
                List<SubJob> servicesList = ((Expert) personList.get(i)).getServicesList();
                personDtoS.get(i).getSubJob().addAll(MapperServices.INSTANCE.listSubJobToSubJobDtoRes(servicesList));
                personDtoS.get(i).setPerformance(((Expert) personList.get(i)).getPerformance());
            }
        }
        if (personDtoS.isEmpty())
            throw new NotFoundException("not found any person in this search");
        return personDtoS;
    }

    private void subService(AdminRequestDto adminRequestDto) {
        SubJob subJob = subJobService.findSubJobByName(adminRequestDto.getSubService());
        adminRequestDto.setSubSubject(subJob);
    }

    private void maxMin(AdminRequestDto adminRequestDto) {
        if (adminRequestDto.getTypePerformance().equals("max"))
            adminRequestDto.setPerformance(expertService.findMax());
        else
            adminRequestDto.setPerformance(expertService.findMin());
    }
}