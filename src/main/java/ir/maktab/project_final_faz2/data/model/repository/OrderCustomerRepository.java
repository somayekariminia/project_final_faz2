package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestOrderDto;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface OrderCustomerRepository extends JpaRepository<OrderCustomer, Long>, JpaSpecificationExecutor<OrderCustomer> {
    @Query("select  o from OrderCustomer o where o.subJob=:subJob and (o.orderStatus='WaitingSelectTheExpert' or  o.orderStatus='WaitingForOfferTheExperts')")
    List<OrderCustomer> findAllBySubJobForAExpert(@Param("subJob") SubJob subJob);
    List<OrderCustomer> findAllByCustomer(Customer customer);
    List<OrderCustomer>findAllOrderExpert(); static Specification<OrderCustomer> searchOrderCustomerByCriteria(AdminRequestOrderDto request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getOrderStatus() != null && !request.getOrderStatus().toString().isEmpty())
                predicates.add(builder.equal(root.get("orderStatus"), request.getOrderStatus()));

            if (request.getPrice() != null && request.getPrice().doubleValue()!=0)
                predicates.add(builder.equal(root.get("price"), request.getPrice()));

            if ((request.getLowDateStarter() != null && !request.getLowDateStarter().isEmpty()) && request.getBigDateStater() != null && !request.getBigDateStater().isEmpty() ) {
                LocalDateTime localDateTimeLow=LocalDateTime.parse(request.getLowDateStarter());
                LocalDateTime localDateTimeBig=LocalDateTime.parse(request.getBigDateStater());
                predicates.add(builder.between(root.get("startDateDoWork"),localDateTimeLow,localDateTimeBig));
            }
            if ((request.getLowDateEnd() != null && !request.getLowDateEnd().isEmpty()) && request.getBigDateEnd() != null && !request.getBigDateEnd().isEmpty() ) {
                LocalDateTime localDateTimeLow=LocalDateTime.parse(request.getLowDateEnd());
                LocalDateTime localDateTimeBig=LocalDateTime.parse(request.getBigDateEnd());
                predicates.add(builder.between(root.get("startDateDoWork"),localDateTimeLow,localDateTimeBig));
            }
            if(request.getSubJob()!=null)
                predicates.add(builder.equal(root.get("subJob"),request.getSubJob()));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }



}
