package ir.maktab.project_final_faz2.data.model.entity;

import ir.maktab.project_final_faz2.data.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Temporal(value = TemporalType.DATE)
    @CreationTimestamp
    private Date RegistrationDate;

}
