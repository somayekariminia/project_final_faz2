package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offers, Long> {
    @Query("select  o from Offers  o where o.orderCustomer=:orderCustomer order by o.offerPriceByExpert asc")
    List<Offers> findAllByOrderCustomerOrderByPriceOrder(@Param("orderCustomer") OrderCustomer orderCustomer);

    @Query("select o from Offers o where o.orderCustomer=:orderCustomer and o.isAccept=true")
    Optional<Offers> findOffersIsAccept(OrderCustomer orderCustomer);
}
