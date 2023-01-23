package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.repository.BasicJobRepository;
import ir.maktab.project_final_faz2.data.model.repository.SubJobRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.NullableException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubJobServiceImpl {
   private final SubJobRepository subJobRepository;
  private final BasicJobRepository basicJobRepository;
    public void saveSubJob(SubJob subJob) {
        checkSubJob(subJob);
        subJobRepository.save(subJob);
    }
    private void checkSubJob(SubJob subJob) {
        BasicJob basicJob = basicJobRepository.findBasicJobByNameBase(subJob.getBasicJob().getNameBase()).orElseThrow(()->new NotFoundException("is not exist basicJob to name"+subJob.getBasicJob().getNameBase()));
        if (Objects.isNull(basicJob))
            throw new NotFoundException("basic job is null");
        SubJob subJob1 = findSubJobByName(subJob.getSubJobName());
        if (Objects.nonNull(subJob1))
            throw new RepeatException("this subService Already saved");
    }

    public List<SubJob> findAllSubJob() {
        return subJobRepository.findAll();
    }

    public void updateSubJob(SubJob subJob) {
        SubJob subJobDb=findSubJobByName(subJob.getSubJobName());
        subJobDb.setDescription(subJob.getDescription());
        subJobDb.setPrice(subJob.getPrice());
        subJobRepository.save(subJobDb);
    }
    public SubJob findSubJobByName(String name) {
        return subJobRepository.findBySubJobName(name).orElseThrow(() -> new NotFoundException("is not exist subJob to name"+name));
    }
    }

