package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCustomerRepository extends JpaRepository<OrderCustomer, Long>, JpaSpecificationExecutor<OrderCustomer> {
    @Query("select  o from OrderCustomer o where o.subJob=:subJob and (o.orderStatus='WaitingSelectTheExpert' or  o.orderStatus='WaitingForOfferTheExperts')")
    List<OrderCustomer> findAllBySubJobForAExpert(@Param("subJob") SubJob subJob);

    @Query("select o from OrderCustomer o where o.customer=:customer and  o.orderStatus=:orderStatus")
    List<OrderCustomer> findAllOrderCustomer(@Param("customer") Customer customer,@Param("orderStatus") OrderStatus orderStatus);
    List<OrderCustomer> findAllByCustomer(Customer customer);
    List<OrderCustomer> findAll(Specification<OrderCustomer> sf);

}
