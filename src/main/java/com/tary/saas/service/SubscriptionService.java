package com.tary.saas.service;

import com.tary.saas.entity.Subscription;
import com.tary.saas.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getSubscriptionsByCompanyId(Long companyId) {
        return subscriptionRepository.findByCompanyId(companyId);
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public void cancelSubscription(Long id) {
        subscriptionRepository.findById(id).ifPresent(subscription -> {
            subscription.setStatus(Subscription.Status.CANCELADA);
            subscriptionRepository.save(subscription);
        });
    }
}
