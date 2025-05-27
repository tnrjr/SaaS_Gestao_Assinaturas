package com.tary.saas.repository;

import com.tary.saas.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCompanyId(Long companyId);
    List<Invoice> findByCompanyIdAndStatus(Long companyId, Invoice.Status status);

}
