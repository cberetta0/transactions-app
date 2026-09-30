package com.mendel.transactions_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateTransaction() throws Exception {

        mockMvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 5000,
                                    "type": "cars"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void shouldReturnTransactionIdsByType() throws Exception {

        mockMvc.perform(put("/transactions/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 10000,
                                    "type": "shopping"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(put("/transactions/21")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 5000,
                                    "type": "shopping"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/transactions/types/shopping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasItems(20, 21)));
    }

    @Test
    void shouldCalculateTransactionSum() throws Exception {

        mockMvc.perform(put("/transactions/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 5000,
                                    "type": "cars"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/transactions/sum/30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(5000));
    }

    @Test
    void shouldCalculateTransitiveTransactionSum() throws Exception {

        mockMvc.perform(put("/transactions/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 5000,
                                    "type": "cars"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(put("/transactions/11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 10000,
                                    "type": "shopping",
                                    "parent_id": 10
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(put("/transactions/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 5000,
                                    "type": "shopping",
                                    "parent_id": 11
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/transactions/sum/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(20000));

        mockMvc.perform(get("/transactions/sum/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(15000));

        mockMvc.perform(get("/transactions/sum/12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(5000));
    }
}