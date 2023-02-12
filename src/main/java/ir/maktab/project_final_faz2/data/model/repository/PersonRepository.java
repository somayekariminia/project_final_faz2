package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.dto.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    List<Person> findAll(Specification<Person> specification);

 static Specification<Person> withDynamicQuery(AdminRequestDto request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getEmail() == null && request.getEmail().isEmpty())
                predicates.add(root.isNotNull());
            predicates.add(builder.equal(root.get("email"), request.getEmail()));
            if (request.getLastName() == null && request.getLastName().isEmpty())
                predicates.add(root.isNotNull());
            predicates.add(builder.equal(root.get("lastName"), request.getLastName()));
            if (request.getName() != null && !request.getName().isEmpty()) {
                predicates.add(builder.equal(root.get("firstName"), request.getName()));
            }
            if (request.getRole() != null && !request.getRole().isEmpty()) {
                predicates.add(builder.equal(root.get("role"), request.getRole()));
            }
            if (request.getPerformanceExpert() > 0 || request.getPerformanceExpert() <= 0) {
                predicates.add(builder.equal(root.get("performance"), request.getPerformanceExpert()));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

