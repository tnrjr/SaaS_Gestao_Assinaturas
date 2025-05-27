package com.tary.saas.service;

import com.tary.saas.entity.Customer;
import com.tary.saas.entity.Plan;
import com.tary.saas.entity.Subscription;
import com.tary.saas.repository.CustomerRepository;
import com.tary.saas.repository.PlanRepository;
import com.tary.saas.repository.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;

    public Subscription createSubscription(Long customerId, Long planId, Long companyId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plano não encontrado"));

        Subscription subscription = Subscription.builder()
                .customer(customer)
                .plan(plan)
                .companyId(companyId)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1)) // Exemplo: assinatura válida por 1 mês
                .build();

        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getSubscriptionsByCompanyId(Long companyId) {
        return subscriptionRepository.findByCompanyId(companyId);
    }

    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public void cancelSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));

        subscription.setEndDate(LocalDateTime.now()); // Marcar a assinatura como cancelada a partir de hoje
        subscriptionRepository.save(subscription);
    }
}
