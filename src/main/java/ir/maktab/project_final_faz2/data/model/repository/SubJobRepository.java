package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubJobRepository extends JpaRepository<SubJob,Long> {
    Optional<SubJob> findBySubJobName(String name);
}
