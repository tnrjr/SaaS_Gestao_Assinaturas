package com.tary.saas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Enumerated(EnumType.STRING)
    private Status status; // ATIVA, CANCELADA

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public enum Status {
        ATIVA, CANCELADA
    }
}
