package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasicJobRepository extends JpaRepository<BasicJob,Long> {
    Optional<BasicJob> findBasicJobByNameBase(String name);
}
