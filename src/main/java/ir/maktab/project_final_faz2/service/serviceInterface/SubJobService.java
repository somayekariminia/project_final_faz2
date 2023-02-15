package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.entity.SubJob;

import java.util.List;

public interface SubJobService {
    SubJob saveSubJob(SubJob subJob);

    List<SubJob> findAllSubJob();

    SubJob updateSubJob(SubJob subJob);

    SubJob findSubJobByName(String name);

    SubJob findById(Long id);
}
