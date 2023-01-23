package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface OrderCustomerRepository extends JpaRepository<OrderCustomer,Long> {

    @Query("select  o from OrderCustomer o where (o.subJob.id=: id) order by o.offerPrice asc ")
    List<OrderCustomer> findAllBySubJobForAExpert(@Param("id") Long id);
    @Modifying
    @Query("update OrderCustomer o set  o.orderStatus=?1 where o.id =? 2")
    void updateOrderState(@Param("orderStatues") String orderStatus, @Param("id") Long id);

    @Modifying
    @Query("update OrderCustomer o set  o.orderStatus=?1,o.endDate=?1 where o.id =? 2")
    void updateOrderStateAndEndDate(@Param("orderStatues") String orderStatus, @Param("endDate") Date endDate, @Param("id") Long id);

}
