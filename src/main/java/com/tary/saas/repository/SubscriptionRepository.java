package com.tary.saas.repository;

import com.tary.saas.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByCompanyId(Long companyId);

    Optional<Subscription> findByIdAndCompanyId(Long id, Long companyId);
}
