package com.tary.saas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceResponseDTO(
        Long id,
        Long subscriptionId,
        BigDecimal amount,
        LocalDateTime dueDate,
        LocalDateTime paymentDate,
        String status
) {}
