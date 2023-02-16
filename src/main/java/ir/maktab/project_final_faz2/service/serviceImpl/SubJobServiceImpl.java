package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.repository.BasicJobRepository;
import ir.maktab.project_final_faz2.data.model.repository.SubJobRepository;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.NullObjects;
import ir.maktab.project_final_faz2.service.serviceInterface.SubJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class SubJobServiceImpl implements SubJobService {
    private final SubJobRepository subJobRepository;
    private final BasicJobRepository basicJobRepository;
    private final MessageSourceConfiguration messageSource;

    @Override
    public SubJob saveSubJob(SubJob subJob) {
        checkSubJob(subJob);
        return subJobRepository.save(subJob);
    }

    private void checkSubJob(SubJob subJob) {
        if (Objects.isNull(subJob))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        if (basicJobRepository.findBasicJobByNameBase(subJob.getBasicJob().getNameBase()).isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.notFound-object"));
        if (subJobRepository.findBySubJobName(subJob.getSubJobName()).isPresent())
            throw new DuplicateException(messageSource.getMessage("errors.message.duplicate-object"));
    }

    @Override
    public List<SubJob> findAllSubJob() {
        return subJobRepository.findAll();
    }

    @Override
    public SubJob updateSubJob(SubJob subJob) {
        if(Objects.isNull(subJob))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        SubJob subJobDb = findSubJobByName(subJob.getSubJobName());
        subJobDb.setDescription(subJob.getDescription());
        subJobDb.setPrice(subJob.getPrice());
        return subJobRepository.save(subJobDb);
    }

    @Override
    public SubJob findSubJobByName(String name) {
        return subJobRepository.findBySubJobName(name).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-object")));
    }

    @Override
    public SubJob findById(Long id) {
        return subJobRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-object")));
    }

}

