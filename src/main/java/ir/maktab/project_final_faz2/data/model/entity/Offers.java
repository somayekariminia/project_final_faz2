package ir.maktab.project_final_faz2.data.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Offers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    Duration durationWork;

    private BigDecimal offerPriceByExpert;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @OneToOne
    private Expert expert;
    boolean isAccept;

}
