package com.tary.saas.repository;

import com.tary.saas.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCompanyId(Long companyId);
}
