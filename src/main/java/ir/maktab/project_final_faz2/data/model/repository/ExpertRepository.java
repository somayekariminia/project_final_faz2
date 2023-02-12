package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.dto.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ExpertRepository extends JpaRepository<Expert, Long> , JpaSpecificationExecutor<Expert> {
    Optional<Expert> findByEmail(String email);

    @Query("from Expert e where e.specialtyStatus=:SpecialtyStatus")
    List<Expert> findAllExpertIsConfirm(@Param("SpecialtyStatus") SpecialtyStatus specialtyStatus);
    List<Expert> findAll(Specification<Expert> specification);


}
