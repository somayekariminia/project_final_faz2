package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCustomerRepository extends JpaRepository<OrderCustomer, Long> {
    @Query("select  o from OrderCustomer o where o.subJob=:subJob and (o.orderStatus='WaitingSelectTheExpert' or  o.orderStatus='WaitingForOfferTheExperts')")
    List<OrderCustomer> findAllBySubJobForAExpert(@Param("subJob") SubJob subJob);
    List<OrderCustomer> findAllByCustomer(Customer customer);

}
