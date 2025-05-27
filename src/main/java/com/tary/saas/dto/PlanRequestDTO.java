package com.tary.saas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PlanRequestDTO(
        @NotBlank(message = "O nome do plano é obrigatório")
        String name,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal price
) {}
