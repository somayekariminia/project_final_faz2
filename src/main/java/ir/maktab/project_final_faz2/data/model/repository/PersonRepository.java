package ir.maktab.project_final_faz2.data.model.repository;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Transactional
@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    static Specification<Person> withDynamicQuery(AdminRequestDto request) {
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
            if(request.getRegistrationDate()!=null && !request.getRegistrationDate().isEmpty())
            {
                LocalDateTime localDateTime=LocalDateTime.parse(request.getRegistrationDate());
                predicates.add(builder.greaterThanOrEqualTo(root.get("registrationDate"),localDateTime));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    List<Person> findAll(Specification<Person> specification);

    Optional<Person> findByEmail(String userName);
}

