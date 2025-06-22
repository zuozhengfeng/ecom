package com.yabobaozb.ecom.settlement.adapter.rest;


import com.yabobaozb.ecom.order.domain.service.OrderDomainService;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.infra.repository.ISettlementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MerchantSettlementRestAdapterIntegrationTest {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @Autowired
    private ISettlementRepository settlementRepository;

    @Autowired
    private OrderDomainService orderDomainService;

    @BeforeEach
    void setUp() {
        String baseUrl = "/settlement";
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port + baseUrl)
                .build();
    }


    private final long merchantId = 20000L;

    @Test
    void testRecharge_normal() {
        String settleTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        prepareNormal();

        webTestClient.post()
                .uri("/daily?merchantId="+merchantId+"&settleTime="+settleTime)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(0)
                .jsonPath("$.message").isEqualTo("OK")
                .jsonPath("$.data.merchantId").isEqualTo(merchantId)
                .jsonPath("$.data.settleTime").isEqualTo(settleTime)
                .jsonPath("$.data.settleAmount").isEqualTo(new BigDecimal("3409.47"))
                .jsonPath("$.data.diffAmount").isEqualTo("0.0")
                .jsonPath("$.data.settleResult").isEqualTo("结算正常");



        // 验证数据库中的余额是否已更新
        MerchantDailySettlement merchantDailySettlement = settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime);
        assertNotNull(merchantDailySettlement);
        assertEquals( 0,new BigDecimal("3409.47").compareTo(merchantDailySettlement.getSettleAmount()));
    }

    private void prepareNormal() {

        // 下面的数据来自data.sql
        // 创建2个订单
        // 订单1金额是199.99*2+301.19*3=1303.55元
        // 订单2金额是199.99*3+301.19*5=2105.92元
        // 总金额1303.55+2105.92=3409.47元

        // 创建一个订单
        long buyerId = 10999L;
        long merchantId = 20000L;
        long[] skuIds = {30000L, 30999L};
        int[] quantities = {2, 3};
        BigDecimal[] unitPrices = {new BigDecimal("199.99"), new BigDecimal("301.19")};
        String remark = "Integration test order 1";
        orderDomainService.createOrder(buyerId, merchantId, skuIds, quantities, unitPrices, remark);


        // 创建一个订单
        quantities = new int[]{3, 5};
        remark = "Integration test order 2";
        orderDomainService.createOrder(buyerId, merchantId, skuIds, quantities, unitPrices, remark);
    }
}
