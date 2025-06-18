package com.duvva.rewards;

import com.duvva.rewards.controller.RewardController;
import com.duvva.rewards.model.Customer;
import com.duvva.rewards.model.Transaction;
import com.duvva.rewards.repository.DataRepository;
import com.duvva.rewards.service.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataRepository dataRepository;

    @MockBean
    private RewardService rewardService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Customer customer;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "John Doe");
        transaction = new Transaction(1L, 1L, 120, LocalDateTime.of(2025, 4, 10, 10, 0));
    }

    @Test
    void testGetRewards() throws Exception {
        given(dataRepository.getAllCustomers()).willReturn(List.of(customer));
        given(dataRepository.getAllTransactions()).willReturn(List.of(transaction));
        given(rewardService.calculatePoints(120)).willReturn(90);

        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].totalRewards").value(90));
    }

    @Test
    void testAddCustomer() throws Exception {
        mockMvc.perform(post("/api/rewards/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk());
    }

    @Test
    void testAddTransaction() throws Exception {
        mockMvc.perform(post("/api/rewards/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk());
    }
}
