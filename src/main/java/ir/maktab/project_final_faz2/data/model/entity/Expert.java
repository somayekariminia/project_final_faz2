package ir.maktab.project_final_faz2.data.model.entity;

import ir.maktab.data.model.enums.SpecialtyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Expert extends Person {
    @ManyToMany(fetch = FetchType.EAGER)
    List<SubJob> servicesList = new ArrayList<>();
    @ToString.Exclude
    @OneToMany
    List<Review> listComment = new ArrayList<>();
    private double performance;
    @Enumerated(value = EnumType.STRING)
    private SpecialtyStatus specialtyStatus;

    @Lob
    private byte[] expertImage;
}
