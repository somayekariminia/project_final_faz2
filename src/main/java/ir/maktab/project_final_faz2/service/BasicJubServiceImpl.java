package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.repository.BasicJobRepository;
import ir.maktab.project_final_faz2.data.model.repository.SubJobRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.service.interfaces.BasicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicJubServiceImpl implements BasicService {
    private final BasicJobRepository basicJobRepository;
    private final SubJobRepository subJobRepository;

    @Override
    public BasicJob save(BasicJob basicJob) {
        if (basicJobRepository.findBasicJobByNameBase(basicJob.getNameBase()).isPresent())
            throw new RepeatException("already basicJob " + basicJob.getNameBase() + " is Exist");
        return basicJobRepository.save(basicJob);
    }

    @Override
    public List<SubJob> findAllSubJobsABasicJob(String nameBasicJob) {
        List<SubJob> subJobList = subJobRepository.findAllByBasicJobNameBase(nameBasicJob);
        if (subJobList.isEmpty())
            throw new NotFoundException(String.format("!!There arent subJobs for BasicJob " + nameBasicJob));
        return subJobList;
    }

    @Override
    public List<BasicJob> findAllBasicJobs() {
        return basicJobRepository.findAll();
    }

    @Override
    public BasicJob findBasicJobByName(String name) {
        return basicJobRepository.findBasicJobByNameBase(name).orElseThrow(() -> new NotFoundException(String.format("is not exist BasicJob %s ", name)));
    }
}
