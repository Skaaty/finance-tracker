package com.skaaty.financetracker.controller;

import com.skaaty.financetracker.dto.TransactionRequest;
import com.skaaty.financetracker.model.TransactionType;
import com.skaaty.financetracker.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import javax.print.attribute.standard.Media;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void shouldReturnBadRequestsWhenAmountIsNegative() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("-50.00"));
        request.setDate(LocalDate.now());
        request.setType(TransactionType.INCOME);
        request.setUserId(1L);
        request.setCategoryId(1L);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDateIsInFuture() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setDate(LocalDate.now().plusDays(5));
        request.setType(TransactionType.INCOME);
        request.setUserId(1L);
        request.setCategoryId(1L);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}
