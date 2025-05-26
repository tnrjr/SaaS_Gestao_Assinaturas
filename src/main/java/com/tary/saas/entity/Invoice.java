package com.tary.saas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    private LocalDateTime dueDate;
    private LocalDateTime paymentDate;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Status status; // PENDENTE, PAGO, VENCIDO

    public enum Status {
        PENDENTE, PAGO, VENCIDO
    }
}