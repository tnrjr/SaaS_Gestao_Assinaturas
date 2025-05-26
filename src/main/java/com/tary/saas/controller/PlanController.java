package com.tary.saas.controller;

import com.tary.saas.entity.Plan;
import com.tary.saas.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan, HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");
        plan.setCompanyId(companyId);
        return ResponseEntity.ok(planService.createPlan(plan));
    }

    @GetMapping
    public ResponseEntity<List<Plan>> getPlans(HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");
        return ResponseEntity.ok(planService.getPlansByCompanyId(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable Long id) {
        return planService.getPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
