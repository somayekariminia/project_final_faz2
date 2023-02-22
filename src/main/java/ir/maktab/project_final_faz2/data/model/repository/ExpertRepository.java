package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ExpertRepository extends JpaRepository<Expert, Long>, JpaSpecificationExecutor<Expert> {
    Optional<Expert> findByEmail(String email);

    @Query("from Expert e where e.specialtyStatus=:SpecialtyStatus")
    List<Expert> findAllExpertIsConfirm(@Param("SpecialtyStatus") SpecialtyStatus specialtyStatus);

    @Query("select Max(e.performance) from Expert e")
    double maxPerformance();

    @Query("select Min(e.performance) from Expert e ")
    double minPerformance();

    Optional<Expert> findByCodeValidate(String codeValidation);
}

