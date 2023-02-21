package ir.maktab.project_final_faz2.data.model.entity;

import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

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

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Credit credit;

    private double performance;

    @Enumerated(value = EnumType.STRING)
    private SpecialtyStatus specialtyStatus;

    @Lob
    private byte[] expertImage;

    @Transient
    private MultipartFile multipartFile;

}
