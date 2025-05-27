package com.tary.saas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tary.saas.config.SecurityConfig;
import com.tary.saas.dto.InvoiceRequestDTO;
import com.tary.saas.entity.Invoice;
import com.tary.saas.entity.Subscription;
import com.tary.saas.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InvoiceController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import({SecurityConfig.class, InvoiceControllerTest.TestConfig.class})
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private InvoiceService invoiceService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public InvoiceService invoiceService() {
            return org.mockito.Mockito.mock(InvoiceService.class);
        }
    }

    private Invoice sampleInvoice;

    @BeforeEach
    void setup() {
        Subscription subscription = Subscription.builder().id(1L).build();

        sampleInvoice = Invoice.builder()
                .id(1L)
                .companyId(1L)
                .subscription(subscription)
                .amount(new BigDecimal("100.00"))
                .dueDate(LocalDateTime.now().plusDays(30))
                .status(Invoice.Status.PENDENTE)
                .build();
    }

    @Test
    void testCreateInvoice() throws Exception {
        InvoiceRequestDTO requestDTO = new InvoiceRequestDTO(1L, new BigDecimal("100.00"));

        when(invoiceService.createInvoice(any(), any(), any())).thenReturn(sampleInvoice);

        mockMvc.perform(post("/api/invoices")
                        .header("Authorization", "Bearer token")
                        .requestAttr("companyId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void testGetInvoices() throws Exception {
        when(invoiceService.getInvoicesByCompanyId(1L)).thenReturn(List.of(sampleInvoice));

        mockMvc.perform(get("/api/invoices")
                        .header("Authorization", "Bearer token")
                        .requestAttr("companyId", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testGetInvoiceById() throws Exception {
        when(invoiceService.getInvoiceById(1L)).thenReturn(Optional.of(sampleInvoice));

        mockMvc.perform(get("/api/invoices/1")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testPayInvoice() throws Exception {
        sampleInvoice.setStatus(Invoice.Status.PAGO);
        when(invoiceService.payInvoice(1L)).thenReturn(sampleInvoice);

        mockMvc.perform(post("/api/invoices/1/pay")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAGO"));
    }

    @Test
    void testUpdateStatuses() throws Exception {
        mockMvc.perform(post("/api/invoices/update-status")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }
}
