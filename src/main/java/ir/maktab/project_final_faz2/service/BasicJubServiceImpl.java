package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.repository.BasicJobRepository;
import ir.maktab.project_final_faz2.data.model.repository.SubJobRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicJubServiceImpl {
   private final BasicJobRepository basicJobRepository;
    private final SubJobRepository subJobRepository;

    public void save(BasicJob basicJob) {
        BasicJob basicJobDb = findByName(basicJob.getNameBase());
        if (Objects.nonNull(basicJobDb))
            throw new RepeatException("this basicService already in db");
        basicJobRepository.save(basicJob);
    }
    public BasicJob findByName(String name) {
        return basicJobRepository.findBasicJobByNameBase(name).orElseThrow(() -> new NotFoundException("SubJob is Null!!!"));
    }
    public List<SubJob> findAllSubJobsABasicJob(String nameBasicJob) {
        return subJobRepository.findAllByBasicJob_NameBase(nameBasicJob);
    }
    public List<BasicJob> findAllBasicJobs() {
        return basicJobRepository.findAll();
    }
}
