package com.tary.saas.service;

import com.tary.saas.entity.Invoice;
import com.tary.saas.entity.Subscription;
import com.tary.saas.repository.InvoiceRepository;
import com.tary.saas.repository.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final SubscriptionRepository subscriptionRepository;

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    public Invoice createInvoice(Long subscriptionId, BigDecimal amount, Long companyId) {
        Subscription subscription = subscriptionRepository.findByIdAndCompanyId(subscriptionId, companyId)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada para essa empresa."));

        Invoice invoice = Invoice.builder()
                .subscription(subscription)
                .companyId(companyId)
                .amount(amount)
                .dueDate(LocalDateTime.now().plusDays(30)) // Vencimento para 30 dias
                .paymentDate(null) // Ainda não foi pago
                .status(Invoice.Status.PENDENTE)
                .build();

        Invoice saved = invoiceRepository.save(invoice);
        log.info("Fatura {} criada para assinatura {} com vencimento em {}", saved.getId(), subscriptionId, saved.getDueDate());
        return saved;
    }

    public List<Invoice> getInvoicesByCompanyId(Long companyId) {
        return invoiceRepository.findByCompanyId(companyId);
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice payInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Fatura não encontrada"));

        if (invoice.getStatus() == Invoice.Status.PAGO) {
            throw new IllegalStateException("Fatura já está paga");
        }

        invoice.setPaymentDate(LocalDateTime.now());
        invoice.setStatus(Invoice.Status.PAGO);

        Invoice updated = invoiceRepository.save(invoice);
        log.info("Fatura {} foi paga em {}", updated.getId(), updated.getPaymentDate());
        return updated;
    }

    public void updateOverdueInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        int updatedCount = 0;

        for (Invoice invoice : invoices) {
            if (invoice.getStatus() == Invoice.Status.PENDENTE
                    && invoice.getDueDate().isBefore(LocalDateTime.now())) {

                invoice.setStatus(Invoice.Status.VENCIDO);
                invoiceRepository.save(invoice);
                updatedCount++;

                log.info("Fatura {} marcada como VENCIDO (vencimento em: {})", invoice.getId(), invoice.getDueDate());
            }
        }

        log.info("Atualização de status finalizada. Faturas atualizadas: {}", updatedCount);
    }

    @Scheduled(cron = "0 0 0 * * *") // Todos os dias à meia-noite
    public void updateOverdueInvoicesScheduled() {
        log.info("Iniciando atualização automática de faturas vencidas...");
        updateOverdueInvoices();
    }

    public List<Invoice> getInvoicesByCompanyIdAndStatus(Long companyId, Invoice.Status status) {
        return invoiceRepository.findByCompanyIdAndStatus(companyId, status);
    }

}
