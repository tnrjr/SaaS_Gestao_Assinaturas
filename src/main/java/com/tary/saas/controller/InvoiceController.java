package com.tary.saas.controller;

import com.tary.saas.dto.InvoiceRequestDTO;
import com.tary.saas.dto.InvoiceResponseDTO;
import com.tary.saas.entity.Invoice;
import com.tary.saas.service.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody @Valid InvoiceRequestDTO dto, HttpServletRequest request) {
        Long companyId = (Long) request.getAttribute("companyId");

        Invoice invoice = invoiceService.createInvoice(
                dto.subscriptionId(),
                dto.amount(),
                companyId
        );

        return ResponseEntity.ok(toResponse(invoice));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDTO>> getInvoices(
            HttpServletRequest request,
            @RequestParam(value = "status", required = false) Invoice.Status status) {

        Long companyId = (Long) request.getAttribute("companyId");

        List<Invoice> invoices = (status != null)
                ? invoiceService.getInvoicesByCompanyIdAndStatus(companyId, status)
                : invoiceService.getInvoicesByCompanyId(companyId);

        List<InvoiceResponseDTO> response = invoices.stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDTO> getInvoice(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<InvoiceResponseDTO> pay(@PathVariable Long id) {
        Invoice invoice = invoiceService.payInvoice(id);
        return ResponseEntity.ok(toResponse(invoice));
    }

    @PostMapping("/update-status")
    public ResponseEntity<Void> updateStatuses() {
        invoiceService.updateOverdueInvoices();
        return ResponseEntity.ok().build();
    }

    private InvoiceResponseDTO toResponse(Invoice invoice) {
        return new InvoiceResponseDTO(
                invoice.getId(),
                invoice.getSubscription().getId(),
                invoice.getAmount(),
                invoice.getDueDate(),
                invoice.getPaymentDate(),
                invoice.getStatus().name()
        );
    }
}
