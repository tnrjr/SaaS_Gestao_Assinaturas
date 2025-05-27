package com.tary.saas.service;

import com.tary.saas.entity.Invoice;
import com.tary.saas.entity.Subscription;
import com.tary.saas.repository.InvoiceRepository;
import com.tary.saas.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvoiceServiceTest {

    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvoice_success() {
        // Arrange
        Subscription subscription = Subscription.builder().id(1L).build();
        Mockito.when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Invoice expected = Invoice.builder()
                .id(1L)
                .subscription(subscription)
                .companyId(1L)
                .amount(new BigDecimal("100.00"))
                .dueDate(Mockito.any())
                .status(Invoice.Status.PENDENTE)
                .build();

        Mockito.when(invoiceRepository.save(Mockito.any(Invoice.class))).thenReturn(expected);

        // Act
        Invoice result = invoiceService.createInvoice(1L, new BigDecimal("100.00"), 1L);

        // Assert
        assertThat(result.getSubscription().getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualByComparingTo("100.00");
        assertThat(result.getStatus()).isEqualTo(Invoice.Status.PENDENTE);
    }

    @Test
    void testPayInvoice_success() {
        // Arrange
        Invoice invoice = Invoice.builder()
                .id(1L)
                .status(Invoice.Status.PENDENTE)
                .dueDate(LocalDateTime.now().plusDays(1))
                .build();

        Mockito.when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        Mockito.when(invoiceRepository.save(Mockito.any(Invoice.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Invoice result = invoiceService.payInvoice(1L);

        // Assert
        assertThat(result.getStatus()).isEqualTo(Invoice.Status.PAGO);
        assertThat(result.getPaymentDate()).isNotNull();
    }

    @Test
    void testPayInvoice_alreadyPaid_throwsException() {
        // Arrange
        Invoice invoice = Invoice.builder()
                .id(1L)
                .status(Invoice.Status.PAGO)
                .build();

        Mockito.when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> invoiceService.payInvoice(1L));
    }

    @Test
    void testUpdateOverdueInvoices_marksAsOverdue() {
        // Arrange
        Invoice overdueInvoice = Invoice.builder()
                .id(1L)
                .status(Invoice.Status.PENDENTE)
                .dueDate(LocalDateTime.now().minusDays(1))
                .build();

        Mockito.when(invoiceRepository.findAll()).thenReturn(java.util.List.of(overdueInvoice));
        Mockito.when(invoiceRepository.save(Mockito.any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        invoiceService.updateOverdueInvoices();

        // Assert
        assertThat(overdueInvoice.getStatus()).isEqualTo(Invoice.Status.VENCIDO);
    }
}
