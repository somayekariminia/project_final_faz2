package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.repository.BasicJobRepository;
import ir.maktab.project_final_faz2.data.model.repository.SubJobRepository;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.NullObjects;
import ir.maktab.project_final_faz2.service.serviceInterface.BasicService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicJubServiceImpl implements BasicService {
    private final BasicJobRepository basicJobRepository;
    private final SubJobRepository subJobRepository;

    @Override
    public BasicJob save(BasicJob basicJob) {
        if (Objects.isNull(basicJob))
            throw new NullObjects("basicJob is null");
        if (basicJobRepository.findBasicJobByNameBase(basicJob.getNameBase()).isPresent())
            throw new DuplicateException("already basicJob " + basicJob.getNameBase() + " is Exist");
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
