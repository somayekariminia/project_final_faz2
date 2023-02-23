package ir.maktab.project_final_faz2.service.serviceImpl.specification;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestOrderDto;
import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateSpecificationOrder {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Specification<OrderCustomer> searchOrderCustomerByCriteria(AdminRequestOrderDto request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getOrderStatus() != null && !request.getOrderStatus().toString().isEmpty())
                predicates.add(builder.equal(root.get("orderStatus"), request.getOrderStatus()));

            if (request.getPrice() != null && request.getPrice().doubleValue() != 0)
                predicates.add(builder.equal(root.get("offerPrice"), request.getPrice()));

            if ((request.getLowDateStarter() != null && !request.getLowDateStarter().isEmpty()) && request.getBigDateStater() != null && !request.getBigDateStater().isEmpty()) {
                LocalDateTime localDateTimeLow = LocalDateTime.parse(request.getLowDateStarter(), DATE_FORMATTER);
                LocalDateTime localDateTimeBig = LocalDateTime.parse(request.getBigDateStater(), DATE_FORMATTER);

                predicates.add(builder.between(root.get("startDateDoWork"), localDateTimeLow, localDateTimeBig));
            }
            if ((request.getLowOrEqualOrBig() != null && !request.getLowOrEqualOrBig().isEmpty())
                    && (request.getLowDateStarter() != null && !request.getLowDateStarter().isEmpty())) {
                LocalDateTime localDateTimeLow = LocalDateTime.parse(request.getLowDateStarter(), DATE_FORMATTER);
                if (request.getLowOrEqualOrBig().equals("low"))
                    predicates.add(builder.lessThanOrEqualTo(root.get("startDateDoWork"), localDateTimeLow));
                if (request.getLowOrEqualOrBig().equals("big"))
                    predicates.add(builder.greaterThanOrEqualTo(root.get("startDateDoWork"), localDateTimeLow));
                if (request.getLowOrEqualOrBig().equals("equal"))
                    predicates.add(builder.equal(root.get("startDateDoWork"), localDateTimeLow));

            }

            if ((request.getLowOrEqualOrBig() != null && !request.getLowOrEqualOrBig().isEmpty())
                    && (request.getLowDateEnd() != null && !request.getLowDateEnd().isEmpty())) {
                LocalDateTime localDateTimeLow = LocalDateTime.parse(request.getLowDateEnd(), DATE_FORMATTER);
                if (request.getLowOrEqualOrBig().equals("low"))
                    predicates.add(builder.lessThanOrEqualTo(root.get("endDateDoWork"), localDateTimeLow));
                if (request.getLowOrEqualOrBig().equals("big"))
                    predicates.add(builder.greaterThanOrEqualTo(root.get("endDateDoWork"), localDateTimeLow));
                if (request.getLowOrEqualOrBig().equals("equal"))
                    predicates.add(builder.equal(root.get("endDateDoWork"), localDateTimeLow));

            }

            if ((request.getLowDateEnd() != null && !request.getLowDateEnd().isEmpty()) && request.getBigDateEnd() != null && !request.getBigDateEnd().isEmpty()) {
                LocalDateTime localDateTimeLow = LocalDateTime.parse(request.getLowDateEnd(), DATE_FORMATTER);
                LocalDateTime localDateTimeBig = LocalDateTime.parse(request.getBigDateEnd(), DATE_FORMATTER);

                predicates.add(builder.between(root.get("endDateDoWork"), localDateTimeLow, localDateTimeBig));
            }

            if (request.getSubService() != null && !request.getSubService().isEmpty()) {
                Join<OrderCustomer, SubJob> joinSubJob = root.join("subJob");
                predicates.add(builder.equal(joinSubJob.<String>get("subJobName"), request.getSubService()));
            }
            if (request.getBasicService() != null && !request.getBasicService().isEmpty()) {
                Join<OrderCustomer, SubJob> joinSubJob = root.join("subJob");
                Join<SubJob, BasicJob> joinBasicJob = joinSubJob.join("basicJob");
                predicates.add(builder.equal(joinBasicJob.get("nameBase"), request.getBasicService()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
