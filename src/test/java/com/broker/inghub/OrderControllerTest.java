package com.broker.inghub;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "password"; // matches seeded bcrypt
    private static final String CUST1_USER = "cust1";
    private static final String CUST1_PASS = "password";
    private static final String CUSTOMER_ID = "4"; // cust1 id per data.sql

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String loginAndGetToken(String username, String password) throws Exception {
        String loginJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();
        String resp = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(resp);
        return node.get("token").asText();
    }

    private JsonNode createOrder(String token, String side) throws Exception {
        String body = String.format("{\"customerId\":\"%s\",\"assetName\":\"%s\",\"side\":\"%s\",\"size\":%d,\"price\":%d}",
                OrderControllerTest.CUSTOMER_ID, "TRY", side, 10, 5);
        MvcResult result = mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private void cancelOrder(String token, long orderId) throws Exception {
        mockMvc.perform(delete("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void createBuyOrder_andCancel() throws Exception {
        String token = loginAndGetToken(ADMIN_USER, ADMIN_PASS);

        JsonNode created = createOrder(token, "BUY");
        long orderId = created.get("id").asLong();

        // list and assert contains this customer
        mockMvc.perform(get("/api/orders?customerId=" + CUSTOMER_ID)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].customerId", hasItem(CUSTOMER_ID)));

        // cancel created order
        cancelOrder(token, orderId);
    }

    @Test
    void createSellOrder_andCancel() throws Exception {
        String token = loginAndGetToken(CUST1_USER, CUST1_PASS);

        JsonNode created = createOrder(token, "SELL");
        long orderId = created.get("id").asLong();

        // cancel created order
        cancelOrder(token, orderId);
    }

    @Test
    void createBuyOrder_insufficientFunds_returnsBadRequest() throws Exception {
        String token = loginAndGetToken(ADMIN_USER, ADMIN_PASS);
        String body = String.format("{\"customerId\":\"%s\",\"assetName\":\"%s\",\"side\":\"%s\",\"size\":%d,\"price\":%d}",
                CUSTOMER_ID, "ABC", "BUY", 100000, 100);
        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Insufficient TRY balance for buy order"));
    }
}
