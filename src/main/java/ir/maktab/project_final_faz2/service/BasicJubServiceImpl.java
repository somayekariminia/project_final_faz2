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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicJubServiceImpl {
    private final BasicJobRepository basicJobRepository;
    private final SubJobRepository subJobRepository;

    public BasicJob save(BasicJob basicJob) {
        if (basicJobRepository.findBasicJobByNameBase(basicJob.getNameBase()).isPresent())
            throw new RepeatException("already basicJob to exist name: " + basicJob.getNameBase());
        return basicJobRepository.save(basicJob);
    }
    public List<SubJob> findAllSubJobsABasicJob(String nameBasicJob) {
        return subJobRepository.findAllByBasicJob_NameBase(nameBasicJob);
    }
    public List<BasicJob> findAllBasicJobs() {
        return basicJobRepository.findAll();
    }
    public BasicJob findBasicJobByName(String name){
        return basicJobRepository.findBasicJobByNameBase(name).orElseThrow(()->new NotFoundException("is not exist"));

    }
}