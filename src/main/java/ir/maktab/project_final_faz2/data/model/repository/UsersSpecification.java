package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.dto.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
@Component
public class UsersSpecification {
     static Specification<Person> withDynamicQuery(AdminRequestDto request) {
        return  (personRoot, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("email"), request.getEmail()));
            }
            if (request.getName() != null && !request.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")),
                        "%" + request.getName().toLowerCase() + "%"));
            }
            if (request.getGender() != null && !request.getGender().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), request.getGender()));
            }
            query.orderBy(criteriaBuilder.desc(root.get("experience")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

