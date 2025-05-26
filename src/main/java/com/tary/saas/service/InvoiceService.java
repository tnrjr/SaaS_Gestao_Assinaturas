package com.tary.saas.service;

import com.tary.saas.entity.Invoice;
import com.tary.saas.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getInvoicesByCompanyId(Long companyId) {
        return invoiceRepository.findByCompanyId(companyId);
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public void markInvoiceAsPaid(Long id) {
        invoiceRepository.findById(id).ifPresent(invoice -> {
            invoice.setStatus(Invoice.Status.PAGO);
            invoice.setPaymentDate(LocalDateTime.now());
            invoiceRepository.save(invoice);
        });
    }
}
