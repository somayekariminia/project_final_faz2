package ir.maktab.project_final_faz2.service.serviceImpl.specification;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateSpecificationAdmin {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Specification<Person> withDynamicQuery(AdminRequestDto request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getEmail() != null && !request.getEmail().isEmpty())
                predicates.add(builder.like(root.get("email"), request.getEmail()));

            if (request.getLastName() != null && !request.getLastName().isEmpty())
                predicates.add(builder.like(root.get("lastName"), request.getLastName()));

            if (request.getFirstName() != null && !request.getFirstName().isEmpty())
                predicates.add(builder.like(root.get("firstName"), request.getFirstName()));

            if (request.getRole() != null && !request.getRole().toString().isEmpty())
                predicates.add(builder.equal(root.get("role"), request.getRole()));

            if (request.getSubService() != null && !request.getSubService().isEmpty()) {
                Join<Expert, SubJob> servicesList = root.join("servicesList");
                predicates.add(builder.equal(servicesList.get("subJobName"), request.getSubService()));
            }


            if (request.getMinOrMax().equals("min") || request.getMinOrMax().equals("max"))
                predicates.add(builder.equal(root.get("performance"), request.getPerformance()));

            if (request.getRegistrationDate() != null && !request.getRegistrationDate().isEmpty() && request.getLowOrBigOrEqual() != null && !request.getLowOrBigOrEqual().isEmpty()) {
                LocalDate localDate = LocalDate.parse(request.getRegistrationDate(), DATE_FORMATTER);
                if (request.getLowOrBigOrEqual().equals("low"))
                    predicates.add(builder.lessThanOrEqualTo(root.get("registrationDate"), localDate));
                else if (request.getLowOrBigOrEqual().equals("big"))
                    predicates.add(builder.greaterThanOrEqualTo(root.get("registrationDate"), localDate));
                else predicates.add(builder.greaterThanOrEqualTo(root.get("registrationDate"), localDate));
            }

            if (request.getRegistrationDate() != null && !request.getRegistrationDate().isEmpty() && request.getRegistrationDateTwo() != null && !request.getRegistrationDateTwo().isEmpty()) {
                LocalDate localDate = LocalDate.parse(request.getRegistrationDate(), DATE_FORMATTER);
                LocalDate localDateTwo = LocalDate.parse(request.getRegistrationDateTwo(), DATE_FORMATTER);
                predicates.add(builder.between(root.get("registrationDate"), localDate, localDateTwo));
            }

            if (request.getLowOrBigOrEqual() != null && !request.getLowOrBigOrEqual().isEmpty() && request.getNumberOrderCustomer() != null && !request.getNumberOrderCustomer().isEmpty()) {
                if (request.getLowOrBigOrEqual().equals("low"))
                    predicates.add(builder.lessThanOrEqualTo(root.get("numberOrdersRegister"), Integer.parseInt(request.getNumberOrderCustomer())));
                else if (request.getLowOrBigOrEqual().equals("big"))
                    predicates.add(builder.greaterThanOrEqualTo(root.get("numberOrdersRegister"), Integer.parseInt(request.getNumberOrderCustomer())));
                else
                    predicates.add(builder.equal(root.get("numberOrdersRegister"), Integer.parseInt(request.getNumberOrderCustomer())));
            }

            if (request.getLowOrBigOrEqual() != null && !request.getLowOrBigOrEqual().isEmpty()
                    && request.getNumberOrderDone() != null && !request.getNumberOrderDone().isEmpty()) {

                if (request.getLowOrBigOrEqual().equals("low"))
                    predicates.add(builder.lessThanOrEqualTo(root.get("numberOrderDone"), Integer.parseInt(request.getNumberOrderDone())));
                else if (request.getLowOrBigOrEqual().equals("big"))
                    predicates.add(builder.greaterThanOrEqualTo(root.get("numberOrderDone"), Integer.parseInt(request.getNumberOrderDone())));
                else
                    predicates.add(builder.equal(root.get("numberOrderDone"), Integer.parseInt(request.getNumberOrderDone())));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
