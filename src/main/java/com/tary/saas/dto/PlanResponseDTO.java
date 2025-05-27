package com.tary.saas.dto;

import java.math.BigDecimal;

public record PlanResponseDTO(
        Long id,
        String name,
        BigDecimal price
) {}
