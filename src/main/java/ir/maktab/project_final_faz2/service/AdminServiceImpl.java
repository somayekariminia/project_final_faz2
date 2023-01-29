package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.service.interfaces.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final ExpertServiceImpl expertService;

    private final SubJobServiceImpl subJobService;

    private final ExpertRepository expertRepository;

    public AdminServiceImpl(ExpertServiceImpl expertService, SubJobServiceImpl subJobService, ExpertRepository expertRepository) {
        this.expertService = expertService;
        this.subJobService = subJobService;
        this.expertRepository = expertRepository;
    }

    @Override
    public void addExpertToSubJob(Expert expert, SubJob subJob) {
        Expert expertDb = expertService.findByUserName(expert.getEmail());
        SubJob subJobDb = subJobService.findSubJobByName(subJob.getSubJobName());
        if (expertDb.getSpecialtyStatus().equals(SpecialtyStatus.NewState))
            throw new ValidationException(String.format("the Expert %s isNot confirm " + expertDb.getEmail()));
        if (expertDb.getServicesList().stream().anyMatch(subJob1 -> subJob1.getSubJobName().equals(subJobDb.getSubJobName())))
            throw new RepeatException(String.format("%s already exist ", subJob.getSubJobName()));
        expertDb.getServicesList().add(subJobDb);
        expertRepository.save(expertDb);
    }

    @Override
    public void deleteExpertOfSubJob(Expert expert, SubJob subJob) {
        Expert expertDb = expertService.findByUserName(expert.getEmail());
        if (expertDb.getServicesList().stream().noneMatch(subJob1 -> subJob1.getSubJobName().equals(subJob.getSubJobName())))
            throw new NotFoundException(String.format("there arent subJob for the Expert %s !!! ", expert.getEmail()));
        if (expertDb.getServicesList().isEmpty())
            throw new NotFoundException(String.format("list subJubs Expert %s is Null !!!",expertDb.getEmail()));
        expertDb.getServicesList().remove(subJob);
        expertRepository.save(expertDb);
    }

    @Override
    public void isConfirmExpertByAdmin(String userName) {
        Expert expertDb = expertService.findByUserName(userName);
        if (expertDb.getSpecialtyStatus().equals(SpecialtyStatus.Confirmed))
            throw new ValidationException("expert is confirm");
        expertDb.setSpecialtyStatus(SpecialtyStatus.Confirmed);
        expertRepository.save(expertDb);
    }

    @Override
    public List<Expert> findAllExpertIsNtConFirm() {
        return expertService.findAllExpertsApproved();
    }

    @Override
    public List<Expert> findAllIsConfirm() {
        return expertService.findAllExpertsIsNotConfirm();
    }

}