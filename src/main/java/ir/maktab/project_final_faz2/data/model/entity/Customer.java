package ir.maktab.project_final_faz2.data.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
public class Customer extends Person {
    @OneToMany
    List<OrderRegistration> orderRegistrationList = new ArrayList<>();
}
