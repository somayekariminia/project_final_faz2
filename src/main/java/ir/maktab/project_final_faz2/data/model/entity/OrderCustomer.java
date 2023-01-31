package ir.maktab.project_final_faz2.data.model.entity;


import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
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
public class OrderCustomer {
    @OneToOne(cascade = CascadeType.PERSIST)
    Address address;
    @ManyToOne
    Customer customer;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal offerPrice;
    private String aboutWork;
    @Column(unique = true)
    private String codeOrder;
    @Temporal(value = TemporalType.DATE)
    private Date StartDateDoWork;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endDateDoWork;
    @OneToOne
    private SubJob subJob;
}
