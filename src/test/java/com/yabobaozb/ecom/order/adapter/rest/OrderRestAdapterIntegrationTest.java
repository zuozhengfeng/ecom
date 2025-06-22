package com.yabobaozb.ecom.order.adapter.rest;


import com.yabobaozb.ecom.buyer.infra.repository.IBuyerBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderRestAdapterIntegrationTest {


    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @Autowired
    private IBuyerBalanceRepository buyerBalanceRepository;

    @BeforeEach
    void setUp() {
        String baseUrl = "/order";
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + baseUrl)
                .build();
    }

    @Test
    void testCreate_normal() {
        long buyerId = 10999L;
        long merchantId = 20000L;
        long[] skuIds = {30000L, 30999L};
        int[] quantities = {2, 3};
        BigDecimal[] unitPrices = {new BigDecimal("199.99"), new BigDecimal("301.19")};
        String remark = "Integration test order";

        webTestClient.post()
                // .uri("/create?buyerId="+buyerId+"&amount="+amount)
                .uri(uriBuilder -> uriBuilder
                        .path("/create")
                        .queryParam("buyerId", buyerId)
                        .queryParam("merchantId", merchantId)
                        .queryParam("skuIds", (Object[])Arrays.stream(skuIds).boxed().toArray(Long[]::new))
                        .queryParam("quantities", (Object[])Arrays.stream(quantities).boxed().toArray(Integer[]::new))
                        .queryParam("unitPrices", (Object[])unitPrices)
                        .queryParam("remark", remark)
                        .build()
                    )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.message").isEqualTo("OK")
                .jsonPath("$.data.orderId").isNumber();
    }



}
