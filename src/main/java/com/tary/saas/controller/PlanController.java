package com.tary.saas.controller;

import com.tary.saas.dto.PlanRequestDTO;
import com.tary.saas.dto.PlanResponseDTO;
import com.tary.saas.entity.Plan;
import com.tary.saas.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanResponseDTO> createPlan(@RequestBody @Valid PlanRequestDTO dto, HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");

        Plan plan = Plan.builder()
                .companyId(companyId)
                .name(dto.name())
                .price(dto.price())
                .build();

        Plan saved = planService.createPlan(plan);

        PlanResponseDTO response = new PlanResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getPrice()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PlanResponseDTO>> getPlans(HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");

        List<PlanResponseDTO> plans = planService.getPlansByCompanyId(companyId)
                .stream()
                .map(p -> new PlanResponseDTO(p.getId(), p.getName(), p.getPrice()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(plans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponseDTO> getPlan(@PathVariable Long id) {
        return planService.getPlanById(id)
                .map(p -> new PlanResponseDTO(p.getId(), p.getName(), p.getPrice()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}
