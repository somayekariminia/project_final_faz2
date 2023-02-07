package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.Credit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
@Transactional
public interface CreditRepository extends JpaRepository<Credit,Long> {
}
