package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.repository.BasicJobRepository;
import ir.maktab.project_final_faz2.data.model.repository.SubJobRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubJobServiceImpl {
    private final SubJobRepository subJobRepository;
    private final BasicJobRepository basicJobRepository;

    public SubJob saveSubJob(SubJob subJob) {
        checkSubJob(subJob);
        return subJobRepository.save(subJob);
    }

    private void checkSubJob(SubJob subJob) {
        if (basicJobRepository.findBasicJobByNameBase(subJob.getBasicJob().getNameBase()).isEmpty())
            throw new NotFoundException("is not exist basicJob to name" + subJob.getBasicJob().getNameBase());
        if (subJobRepository.findBySubJobName(subJob.getSubJobName()).isPresent())
            throw new RepeatException("this subService Already saved");
    }

    public List<SubJob> findAllSubJob() {
        return subJobRepository.findAll();
    }

    public SubJob updateSubJob(SubJob subJob) {
        SubJob subJobDb = findSubJobByName(subJob.getSubJobName());
        subJobDb.setDescription(subJob.getDescription());
        subJobDb.setPrice(subJob.getPrice());
        return subJobRepository.save(subJobDb);
    }

    public SubJob findSubJobByName(String name) {
        return subJobRepository.findBySubJobName(name).orElseThrow(() -> new NotFoundException("is not exist subJob to name" + name));
    }
    public SubJob findById(Long id) {
        return subJobRepository.findById(id).orElseThrow(() -> new NotFoundException("is not exist subJob to name"));
    }
}

