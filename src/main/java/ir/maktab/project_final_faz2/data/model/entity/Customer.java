package ir.maktab.project_final_faz2.data.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@SuperBuilder
public class Customer extends Person {
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Credit credit;
}
