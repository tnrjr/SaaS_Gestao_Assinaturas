package com.tary.saas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private String name;
    private String description;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Periodicity periodicity; // MENSAL, ANUAL

    public enum Periodicity {
        MENSAL, ANUAL
    }
}
