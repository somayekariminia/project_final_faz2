package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.Admin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
@Transactional
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findAdminByUserName(String userName);
}
