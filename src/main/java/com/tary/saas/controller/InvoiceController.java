package com.tary.saas.controller;

import com.tary.saas.entity.Invoice;
import com.tary.saas.service.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice, HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");
        invoice.setCompanyId(companyId);
        return ResponseEntity.ok(invoiceService.createInvoice(invoice));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getInvoices(HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");
        return ResponseEntity.ok(invoiceService.getInvoicesByCompanyId(companyId));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Void> payInvoice(@PathVariable Long id) {
        invoiceService.markInvoiceAsPaid(id);
        return ResponseEntity.noContent().build();
    }
}
