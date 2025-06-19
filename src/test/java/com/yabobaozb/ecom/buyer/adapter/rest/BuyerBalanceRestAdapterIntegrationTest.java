package com.yabobaozb.ecom.buyer.adapter.rest;


import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.infra.repository.IBuyerBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuyerBalanceRestAdapterIntegrationTest {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @Autowired
    private IBuyerBalanceRepository buyerBalanceRepository;

    @BeforeEach
    void setUp() {
        String baseUrl = "/buyer/balance";
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + baseUrl)
                .build();
    }

    private final long normalBuyerId = 10000L;
    private final long noBalanceBuyerId = 10001L;
    private final long notfoundBuyerId = 10002L;

    @Test
    void testRecharge_normal() {
        final long buyerId = normalBuyerId;
        final String amount = "105.39";

        webTestClient.post()
                .uri("/recharge?buyerId="+buyerId+"&amount="+amount)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.message").isEqualTo("OK");

        // 验证数据库中的余额是否已更新
        BuyerBalance updatedBalance = buyerBalanceRepository.getByBuyerId(buyerId);
        assertNotNull(updatedBalance);
        assertEquals(new BigDecimal(amount).add(new BigDecimal("101.00")), updatedBalance.getBalance());
    }

}
