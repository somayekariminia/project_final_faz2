package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;

import java.util.List;

public interface BasicService {
    BasicJob save(BasicJob basicJob);

    List<SubJob> findAllSubJobsABasicJob(String nameBasicJob);

    List<BasicJob> findAllBasicJobs();

    BasicJob findBasicJobByName(String name);
}
