package com.yabobaozb.ecom.settlement.domain.service;

import com.yabobaozb.ecom.merchant.adapter.local.MerchantLocalAdapter;
import com.yabobaozb.ecom.merchant.adapter.response.MerchantBalanceChangeResponse;
import com.yabobaozb.ecom.order.adapter.local.OrderLocalAdapter;
import com.yabobaozb.ecom.order.adapter.response.OrderAmountInfoResponse;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.infra.repository.ISettlementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MerchantSettlementDomainServiceUnitTest {
    @Mock
    private OrderLocalAdapter orderLocalAdapter;

    @Mock
    private MerchantLocalAdapter merchantLocalAdapter;

    @Mock
    private ISettlementRepository settlementRepository;

    @InjectMocks
    private MerchantSettlementDomainService merchantSettlementDomainService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecuteDailySettlement_NoOrders() {
        // 初始化数据
        long merchantId = 1L;
        String settleTime = "20250620";
        LocalDateTime beginAt = LocalDateTime.now().minusDays(1);
        LocalDateTime endAt = LocalDateTime.now();

        when(orderLocalAdapter.listOrdersByMerchantAndCreateTime(merchantId, beginAt, endAt)).thenReturn(Collections.emptyList());
        when(merchantLocalAdapter.listBalanceChangesByMerchantId(merchantId, beginAt, endAt)).thenReturn(Collections.emptyList());
        when(settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime)).thenReturn(null);

        // 执行
        MerchantDailySettlement result = merchantSettlementDomainService.executeDailySettlement(merchantId, settleTime, beginAt, endAt);

        // 验证
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getExpectAmount());
        assertEquals(BigDecimal.ZERO, result.getSettleAmount());
        verify(settlementRepository, times(1)).saveSettlement(result);
    }

    @Test
    public void testExecuteDailySettlement_Normal() {
        // 初始化数据
        long merchantId = 1L;
        String settleTime = "20250620";
        LocalDateTime beginAt = LocalDateTime.now().minusDays(1);
        LocalDateTime endAt = LocalDateTime.now();

        OrderAmountInfoResponse order1 = mock(OrderAmountInfoResponse.class);
        OrderAmountInfoResponse order2 = mock(OrderAmountInfoResponse.class);
        when(order1.getTotalAmount()).thenReturn(new BigDecimal("100.00"));
        when(order2.getTotalAmount()).thenReturn(new BigDecimal("200.00"));
        List<OrderAmountInfoResponse> orders = List.of(order1, order2);

        MerchantBalanceChangeResponse change1 = mock(MerchantBalanceChangeResponse.class);
        MerchantBalanceChangeResponse change2 = mock(MerchantBalanceChangeResponse.class);
        when(change1.getBalanceFrom()).thenReturn(new BigDecimal("500.00"));
        when(change2.getBalanceTo()).thenReturn(new BigDecimal("800.00"));
        List<MerchantBalanceChangeResponse> balanceChanges = List.of(change1, change2);

        when(orderLocalAdapter.listOrdersByMerchantAndCreateTime(merchantId, beginAt, endAt)).thenReturn(orders);
        when(merchantLocalAdapter.listBalanceChangesByMerchantId(merchantId, beginAt, endAt)).thenReturn(balanceChanges);
        when(settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime)).thenReturn(null);

        // 执行
        MerchantDailySettlement result = merchantSettlementDomainService.executeDailySettlement(merchantId, settleTime, beginAt, endAt);

        // 验证
        assertNotNull(result);
        assertEquals(new BigDecimal("300.00"), result.getExpectAmount());
        assertEquals(new BigDecimal("300.00"), result.getSettleAmount());
        verify(settlementRepository, times(1)).saveSettlement(result);
    }
}