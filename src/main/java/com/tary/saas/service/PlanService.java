package com.tary.saas.service;

import com.tary.saas.entity.Plan;
import com.tary.saas.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public List<Plan> getPlansByCompanyId(Long companyId) {
        return planRepository.findByCompanyId(companyId);
    }

    public Optional<Plan> getPlanById(Long id) {
        return planRepository.findById(id);
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }
}
