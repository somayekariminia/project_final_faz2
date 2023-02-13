package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.AdminRepository;
import ir.maktab.project_final_faz2.data.model.repository.CustomerRepository;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.data.model.repository.PersonRepository;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.ValidationException;
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
                            AdminRepository adminRepository, CustomerRepository customerRepository, PersonRepository personRepository) {
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
            throw new ValidationException(String.format("the Expert %s isNot confirm " , expertDb.getEmail()));
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

    public List<Person> findAllPerson(AdminRequestDto adminRequestDto) {
       Specification personSpecification =PersonRepository.withDynamicQuery(adminRequestDto);
        List<Person> personList=personRepository.findAll(personSpecification);
        if(personList.isEmpty())
            throw new NotFoundException("there arent person");
       return personList;
    }

}