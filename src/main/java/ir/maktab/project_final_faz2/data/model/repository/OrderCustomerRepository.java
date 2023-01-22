package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderCustomerRepository extends JpaRepository<OrderCustomer,Long> {

    @Query("select  o from OrderCustomer o where (o.subJob.id=: id) order by o.offerPrice asc ")
    List<OrderCustomer> findAllBySubJobForAExpert(@Param("id") Long id);
    @Modifying
    @Query("update OrderCustomer o set  o.orderStatus=?1 where o.id =? 2")
    void update(@Param("orderStatues") String orderStatus,Long id);
}
