package com.tary.saas.controller;

import com.tary.saas.entity.Customer;
import com.tary.saas.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer, HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");
        customer.setCompanyId(companyId);
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");
        return ResponseEntity.ok(customerService.getCustomersByCompanyId(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
