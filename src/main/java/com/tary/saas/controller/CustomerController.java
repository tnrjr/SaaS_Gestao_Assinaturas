package com.tary.saas.controller;

import com.tary.saas.dto.CustomerRequestDTO;
import com.tary.saas.dto.CustomerResponseDTO;
import com.tary.saas.entity.Customer;
import com.tary.saas.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody @Valid CustomerRequestDTO dto, HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");

        Customer customer = Customer.builder()
                .companyId(companyId)
                .name(dto.name())
                .email(dto.email())
                .phone(dto.phone())
                .build();

        Customer saved = customerService.createCustomer(customer);

        CustomerResponseDTO response = new CustomerResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhone()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getCustomers(HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");

        List<CustomerResponseDTO> customers = customerService.getCustomersByCompanyId(companyId)
                .stream()
                .map(c -> new CustomerResponseDTO(c.getId(), c.getName(), c.getEmail(), c.getPhone()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(c -> new CustomerResponseDTO(c.getId(), c.getName(), c.getEmail(), c.getPhone()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
