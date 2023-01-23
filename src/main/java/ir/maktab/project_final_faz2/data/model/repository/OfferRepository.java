package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.Offers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offers,Long> {

}
