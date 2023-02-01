package ir.maktab.project_final_faz2.data.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    String numberCard;

    @Column(unique = true,nullable = false)
    String cvv2;

    @Temporal(value = TemporalType.DATE)
    Date expiredDate;

    private BigDecimal balance;
}
