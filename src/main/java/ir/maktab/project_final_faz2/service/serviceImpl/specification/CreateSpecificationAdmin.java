package ir.maktab.project_final_faz2.service.serviceImpl.specification;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateSpecificationAdmin {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

   public static Specification<Person> withDynamicQuery(AdminRequestDto request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getEmail() != null && !request.getEmail().isEmpty())
                predicates.add(builder.equal(root.get("email"), request.getEmail()));

            if (request.getLastName() != null && !request.getLastName().isEmpty())
                predicates.add(builder.equal(root.get("lastName"), request.getLastName()));

            if (request.getFirstName() != null && !request.getFirstName().isEmpty())
                predicates.add(builder.equal(root.get("firstName"), request.getFirstName()));

            if (request.getRole() != null && !request.getRole().toString().isEmpty())
                predicates.add(builder.equal(root.get("role"), request.getRole()));

            if (request.getSubSubject() != null)
                predicates.add(builder.isMember(request.getSubSubject(), root.get("servicesList")));

            if (request.getMinOrMax().equals("min") || request.getMinOrMax().equals("max"))
                predicates.add(builder.equal(root.get("performance"), request.getPerformance()));

            if (request.getRegistrationDate() != null && !request.getRegistrationDate().isEmpty() && request.getLowOrBigOrEqual() != null && !request.getLowOrBigOrEqual().isEmpty()) {
                LocalDateTime localDateTime = LocalDateTime.parse(request.getRegistrationDate(), DATE_FORMATTER);
                if (request.getLowOrBigOrEqual().equals("low"))
                    predicates.add(builder.lessThanOrEqualTo(root.get("registrationDate"), localDateTime));
                else if (request.getLowOrBigOrEqual().equals("big"))
                    predicates.add(builder.greaterThanOrEqualTo(root.get("registrationDate"), localDateTime));
                else predicates.add(builder.greaterThanOrEqualTo(root.get("registrationDate"), localDateTime));
            }
            if (request.getRegistrationDate() != null && request.getRegistrationDate().isEmpty() && request.getRegistrationDateTwo() != null && request.getRegistrationDateTwo().isEmpty()) {
                LocalDateTime localDateTime = LocalDateTime.parse(request.getRegistrationDate(), DATE_FORMATTER);
                LocalDateTime localDateTimeTwo = LocalDateTime.parse(request.getRegistrationDateTwo(), DATE_FORMATTER);
                predicates.add(builder.between(root.get("registrationDate"), localDateTime, localDateTimeTwo));
            }

            if (request.getLowOrBigOrEqual() != null && !request.getLowOrBigOrEqual().isEmpty() && request.getNumberOrderCustomer() != null && request.getNumberOrderCustomer().isEmpty()) {
                if (request.getLowOrBigOrEqual().equals("low"))
                    predicates.add(builder.lessThanOrEqualTo(root.get("numberOrderCustomer"), Integer.parseInt(request.getNumberOrderCustomer())));
                else if (request.getLowOrBigOrEqual().equals("big"))
                    predicates.add(builder.greaterThanOrEqualTo(root.get("numberOrderCustomer"), Integer.parseInt(request.getNumberOrderCustomer())));
                else
                    predicates.add(builder.equal(root.get("numberOrderCustomer"), Integer.parseInt(request.getNumberOrderCustomer())));
            }

            if (request.getLowOrBigOrEqual() != null && !request.getLowOrBigOrEqual().isEmpty() && request.getNumberOrderCustomer() != null && request.getNumberOrderDone().isEmpty()) {
                if (request.getLowOrBigOrEqual().equals("low"))
                    predicates.add(builder.lessThanOrEqualTo(root.get("numberOrderCustomer"), Integer.parseInt(request.getNumberOrderDone())));
                else if (request.getLowOrBigOrEqual().equals("big"))
                    predicates.add(builder.greaterThanOrEqualTo(root.get("numberOrderCustomer"), Integer.parseInt(request.getNumberOrderDone())));
                else
                    predicates.add(builder.equal(root.get("numberOrderCustomer"), Integer.parseInt(request.getNumberOrderDone())));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
