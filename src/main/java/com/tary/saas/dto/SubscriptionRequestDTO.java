package com.tary.saas.dto;

import jakarta.validation.constraints.NotNull;

public record SubscriptionRequestDTO(
        @NotNull(message = "O ID do cliente é obrigatório")
        Long customerId,

        @NotNull(message = "O ID do plano é obrigatório")
        Long planId
) {}
