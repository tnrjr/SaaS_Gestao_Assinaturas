package com.tary.saas.controller;

import com.tary.saas.dto.SubscriptionRequestDTO;
import com.tary.saas.dto.SubscriptionResponseDTO;
import com.tary.saas.entity.Subscription;
import com.tary.saas.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> create(@RequestBody @Valid SubscriptionRequestDTO dto, HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");

        Subscription subscription = subscriptionService.createSubscription(
                dto.customerId(),
                dto.planId(),
                companyId
        );

        return ResponseEntity.ok(toResponse(subscription));
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getAll(HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");

        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getSubscriptionsByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        subscriptionService.cancelSubscription(id);
        return ResponseEntity.noContent().build();
    }

    private SubscriptionResponseDTO toResponse(Subscription subscription) {
        return new SubscriptionResponseDTO(
                subscription.getId(),
                subscription.getCustomer().getId(),
                subscription.getPlan().getId(),
                subscription.getStartDate(),
                subscription.getEndDate()
        );
    }
}
