package com.tary.saas.dto;

import java.time.LocalDateTime;

public record SubscriptionResponseDTO(
        Long id,
        Long customerId,
        Long planId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {}
