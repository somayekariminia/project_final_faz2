package ir.maktab.project_final_faz2.data.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SubJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    BasicJob basicJob;

    @Column(nullable = false)
    private String subJobName;

    private BigDecimal price;

    private String description;
}
