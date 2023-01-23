package ir.maktab.project_final_faz2.data.model.entity;


import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.PERSIST)
    Address address;
    @OneToMany
    List<Offers> offersList = new ArrayList<>();
    private BigDecimal offerPrice;
    private String aboutWork;
    @Column(unique = true)
    private String codeOrder;
    @Temporal(value = TemporalType.DATE)
    private Date offerStartDateCustomer;
    @Enumerated
    private OrderStatus orderStatus;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endDate;
    @OneToOne
    private SubJob subJob;
    @OneToOne
    Expert expert;
}