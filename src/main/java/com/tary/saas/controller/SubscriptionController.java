package com.tary.saas.controller;

import com.tary.saas.entity.Subscription;
import com.tary.saas.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription, HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");
        subscription.setCompanyId(companyId);
        return ResponseEntity.ok(subscriptionService.createSubscription(subscription));
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getSubscriptions(HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByCompanyId(companyId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long id) {
        subscriptionService.cancelSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
