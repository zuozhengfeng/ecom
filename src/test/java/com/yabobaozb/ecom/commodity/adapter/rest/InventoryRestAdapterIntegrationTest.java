package com.yabobaozb.ecom.commodity.adapter.rest;


import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import com.yabobaozb.ecom.commodity.infra.repository.ISkuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryRestAdapterIntegrationTest {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @Autowired
    private ISkuRepository inventoryRepository;

    @BeforeEach
    void setUp() {
        String baseUrl = "/commodity/inventory";
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + baseUrl)
                .build();
    }

    final long normalSkuId = 30000L;
    final long noBalanceSkuId = 30001L;
    final long notFoundSkuId = 30002L;

    @Test
    void testIncreaseInventory_NormalSku() {
        long skuId = normalSkuId;

        SkuInventory skuInventory = inventoryRepository.getBySkuId(skuId);
        int initialInventory = skuInventory.getAvailableInventory();

        int quantity = 10;
        String remark = "test increase";

        int finalInventory = initialInventory + quantity;
        long newVersion = 1+skuInventory.getVersion();

        // 发送 POST 请求
        webTestClient.post()
                .uri("/increase?skuId=" + skuId + "&quantity=" + quantity + "&remark=" + remark)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.message").isEqualTo("OK")
                .jsonPath("$.data").isNotEmpty()
                .jsonPath("$.data.skuId").isEqualTo(skuId)
                .jsonPath("$.data.availableInventory").isEqualTo(finalInventory)
                .jsonPath("$.data.version").isEqualTo(newVersion);

        // 验证数据库是否更新
        skuInventory = inventoryRepository.getBySkuId(skuId);
        assertEquals( skuInventory.getAvailableInventory(), finalInventory );
        assertEquals( skuInventory.getVersion(), newVersion );
    }

    @Test
    void testIncreaseInventory_NoBalanceSku() {
        long skuId = noBalanceSkuId;

        SkuInventory skuInventory = inventoryRepository.getBySkuId(skuId);
        assertNull(skuInventory);
        int initialInventory = 0;

        int quantity = 10;
        String remark = "test increase";

        int finalInventory = initialInventory + quantity;
        long newVersion = 0;

        // 发送 POST 请求
        webTestClient.post()
                .uri("/increase?skuId=" + skuId + "&quantity=" + quantity + "&remark=" + remark)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.message").isEqualTo("OK")
                .jsonPath("$.data").isNotEmpty()
                .jsonPath("$.data.skuId").isEqualTo(skuId)
                .jsonPath("$.data.availableInventory").isEqualTo(finalInventory)
                .jsonPath("$.data.version").isEqualTo(newVersion);

        // 验证数据库是否更新
        skuInventory = inventoryRepository.getBySkuId(skuId);
        assertEquals( skuInventory.getAvailableInventory(), finalInventory );
        assertEquals( skuInventory.getVersion(), newVersion );
    }

    @Test
    void testIncreaseInventory_NotFoundSku() {
        long skuId = notFoundSkuId;
        int quantity = 10;
        String remark = "test increase notFound sku";

        webTestClient.post()
                .uri("/increase?skuId=" + skuId + "&quantity=" + quantity + "&remark=" + remark)
                .exchange()
                .expectStatus().is5xxServerError();
    }


}
