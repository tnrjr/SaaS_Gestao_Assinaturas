package com.tary.saas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record InvoiceRequestDTO(
        @NotNull(message = "O ID da assinatura é obrigatório")
        Long subscriptionId,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal amount
) {}
