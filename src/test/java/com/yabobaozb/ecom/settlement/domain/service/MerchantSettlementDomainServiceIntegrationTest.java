package com.yabobaozb.ecom.settlement.domain.service;

import com.yabobaozb.ecom.order.domain.service.OrderDomainService;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlementRecord;
import com.yabobaozb.ecom.settlement.infra.enums.SettlementResult;
import com.yabobaozb.ecom.settlement.infra.repository.ISettlementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MerchantSettlementDomainServiceIntegrationTest {

    private final MerchantSettlementDomainService merchantSettlementDomainService;

    private final ISettlementRepository settlementRepository;

    private final OrderDomainService orderDomainService;

    @Autowired
    public MerchantSettlementDomainServiceIntegrationTest(MerchantSettlementDomainService merchantSettlementDomainService, ISettlementRepository settlementRepository, OrderDomainService orderDomainService) {
        this.merchantSettlementDomainService = merchantSettlementDomainService;
        this.settlementRepository = settlementRepository;
        this.orderDomainService = orderDomainService;
    }

    // Helper method to create a formatted date string for settlement time
    private String formatSettlementTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
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

    @Test
    void testExecuteDailySettlement_normal_withOrdersAndBalanceChanges() {
        // 准备数据
        prepareNormal();

        // 准备参数
        long merchantId = 20000L;
        LocalDateTime endAt = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime begAt = endAt.minusDays(1);
        String settleTime = formatSettlementTime(begAt);

        // 执行
        MerchantDailySettlement settlement = merchantSettlementDomainService.executeDailySettlement(merchantId, settleTime, begAt, endAt);

        MerchantDailySettlement savedSettlement = settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime);
        assertNotNull(savedSettlement);
        assertEquals(savedSettlement.getVersion(), settlement.getNewVersion());
        assertEquals(savedSettlement.getSettleTime(), settleTime);
        assertEquals(0, savedSettlement.getExpectAmount().compareTo(new BigDecimal("3409.47")));
        assertEquals(0, savedSettlement.getSettleAmount().compareTo(new BigDecimal("3409.47")));
        assertEquals(0, savedSettlement.getDiffAmount().compareTo(new BigDecimal("0")));
        assertEquals(savedSettlement.getRemark(), settlement.getRemark());
        assertEquals(savedSettlement.getSettleResult(), SettlementResult.MATCHED);

        MerchantDailySettlementRecord savedRecord = settlementRepository.getRecentRecordByMerchantAndSettleTime(merchantId, settleTime);
        assertNotNull(savedRecord);
        assertEquals(savedRecord.getVersion(), savedSettlement.getVersion());
        assertEquals(savedRecord.getSettleId(), savedSettlement.getSettleId());
        assertEquals(savedRecord.getSettleTime(), settleTime);
        assertEquals(savedRecord.getBeginAt(), begAt);
        assertEquals(savedRecord.getEndAt(), endAt);
        assertEquals(0, savedRecord.getExpectAmount().compareTo(savedSettlement.getExpectAmount()));
        assertEquals(0, savedRecord.getSettleAmount().compareTo(savedSettlement.getSettleAmount()));
        assertEquals(0, savedRecord.getDiffAmount().compareTo(savedSettlement.getDiffAmount()));
        assertEquals(savedRecord.getSettleResult(), savedSettlement.getSettleResult());
        assertEquals(savedRecord.getRemark(), savedSettlement.getRemark());
        assertEquals(savedRecord.getMerchantId(), merchantId);
    }


    @Test
    void testExecuteDailySettlement_normal_withOrdersAndBalanceChangesAndExistingRecord() {
        // 准备数据
        prepareNormal();

        // 准备参数
        long merchantId = 20000L;
        LocalDateTime endAt = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime begAt = endAt.minusDays(1);
        String settleTime = formatSettlementTime(begAt);

        // 执行2次
        merchantSettlementDomainService.executeDailySettlement(merchantId, settleTime, begAt, endAt);
//        try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//        }
        MerchantDailySettlement settlement = merchantSettlementDomainService.executeDailySettlement(merchantId, settleTime, begAt, endAt);

        MerchantDailySettlement savedSettlement = settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime);
        assertNotNull(savedSettlement);
        assertEquals(savedSettlement.getVersion(), settlement.getNewVersion());
        assertEquals(savedSettlement.getSettleTime(), settleTime);
        assertEquals(0, savedSettlement.getExpectAmount().compareTo(new BigDecimal("3409.47")));
        assertEquals(0, savedSettlement.getSettleAmount().compareTo(new BigDecimal("3409.47")));
        assertEquals(0, savedSettlement.getDiffAmount().compareTo(new BigDecimal("0")));
        assertEquals(savedSettlement.getRemark(), settlement.getRemark());
        assertEquals(savedSettlement.getSettleResult(), SettlementResult.MATCHED);

        MerchantDailySettlementRecord savedRecord = settlementRepository.getRecentRecordByMerchantAndSettleTime(merchantId, settleTime);
        assertNotNull(savedRecord);
        assertEquals(savedRecord.getVersion(), savedSettlement.getVersion());
        assertEquals(savedRecord.getSettleId(), savedSettlement.getSettleId());
        assertEquals(savedRecord.getSettleTime(), settleTime);
        assertEquals(savedRecord.getBeginAt(), begAt);
        assertEquals(savedRecord.getEndAt(), endAt);
        assertEquals(0, savedRecord.getExpectAmount().compareTo(savedSettlement.getExpectAmount()));
        assertEquals(0, savedRecord.getSettleAmount().compareTo(savedSettlement.getSettleAmount()));
        assertEquals(0, savedRecord.getDiffAmount().compareTo(savedSettlement.getDiffAmount()));
        assertEquals(savedRecord.getSettleResult(), savedSettlement.getSettleResult());
        assertEquals(savedRecord.getRemark(), savedSettlement.getRemark());
        assertEquals(savedRecord.getMerchantId(), merchantId);
    }



//    @Test
//    void testExecuteDailySettlement_normal_withExistingSettlement() {
//        // Arrange
//        long merchantId = 1L;
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime yesterday = now.minusDays(1);
//        String settleTime = formatSettlementTime(now);
//
//        // Create test orders
//        OrderAmountInfoResponse order1 = new OrderAmountInfoResponse(1L, 1L, new BigDecimal("100.00"), null, null);
//        OrderAmountInfoResponse order2 = new OrderAmountInfoResponse(2L, 1L, new BigDecimal("50.00"), null, null);
//        List<OrderAmountInfoResponse> orders = Arrays.asList(order1, order2);
//
//        // Create test balance changes
//        MerchantBalanceChangeResponse change1 = new MerchantBalanceChangeResponse(1L, 1L, new BigDecimal("1000.00"), null, null, null);
//        MerchantBalanceChangeResponse change2 = new MerchantBalanceChangeResponse(2L, 1L, null, new BigDecimal("1150.00"), null, null);
//        List<MerchantBalanceChangeResponse> balanceChanges = Arrays.asList(change1, change2);
//
//        // Create an existing settlement
//        LocalDateTime dummyDate = LocalDateTime.now().minusDays(2);
//        MerchantDailySettlement existingSettlement = new MerchantDailySettlement(1L, merchantId, settleTime, dummyDate, dummyDate, BigDecimal.ZERO, BigDecimal.ZERO, 2, dummyDate, dummyDate);
//
//        when(settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime)).thenReturn(existingSettlement);
//
//        // Mock adapter behavior
//        when(orderLocalAdapter.listOrdersByMerchantAndCreateTime(merchantId, yesterday, now))
//                .thenReturn(orders);
//        when(merchantLocalAdapter.listBalanceChangesByMerchantId(merchantId, yesterday, now))
//                .thenReturn(balanceChanges);
//
//        // Act
//        MerchantDailySettlement result = settlementService.executeDailySettlement(
//                merchantId, settleTime, yesterday, now);
//
//        // Assert
//        assertThat(result).isNotNull();
//        assertThat(result.getMerchantId()).isEqualTo(merchantId);
//
//        // 手动验证字段값
//        assertThat(result.getOrderAmount()).isEqualTo(new BigDecimal("150.00"));
//        assertThat(result.getMerchantIncreaseAmount()).isEqualTo(new BigDecimal("150.00"));
//        assertThat(result.getDifferenceAmount()).isEqualTo(BigDecimal.ZERO);
//        assertThat(result.getVersion()).isEqualTo(3); // Should increment version
//
//        // Verify interactions
//        verify(settlementRepository).saveSettlement(result);
//        verify(settlementRepository).getByMerchantAndSettleTime(merchantId, settleTime);
//        verify(orderLocalAdapter).listOrdersByMerchantAndCreateTime(merchantId, yesterday, now);
//        verify(merchantLocalAdapter).listBalanceChangesByMerchantId(merchantId, yesterday, now);
//    }
//
//    @Test
//    void testExecuteDailySettlement_normal_noOrdersOrBalanceChanges() {
//        // Arrange
//        long merchantId = 1L;
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime yesterday = now.minusDays(1);
//        String settleTime = formatSettlementTime(now);
//
//        // Mock repository behavior for no existing settlement
//        when(settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime)).thenReturn(null);
//
//        // Mock adapter behavior with empty lists
//        when(orderLocalAdapter.listOrdersByMerchantAndCreateTime(merchantId, yesterday, now))
//                .thenReturn(List.of());
//        when(merchantLocalAdapter.listBalanceChangesByMerchantId(merchantId, yesterday, now))
//                .thenReturn(List.of());
//
//        // Act
//        MerchantDailySettlement result = settlementService.executeDailySettlement(
//                merchantId, settleTime, yesterday, now);
//
//        // Assert
//        assertThat(result).isNotNull();
//        assertThat(result.getMerchantId()).isEqualTo(merchantId);
//
//        // 手动验证字段值
//        assertThat(result.getOrderAmount()).isEqualTo(BigDecimal.ZERO);
//        assertThat(result.getMerchantIncreaseAmount()).isEqualTo(BigDecimal.ZERO);
//        assertThat(result.getDifferenceAmount()).isEqualTo(BigDecimal.ZERO);
//
//        // Verify interactions
//        verify(settlementRepository).saveSettlement(result);
//        verify(settlementRepository).getByMerchantAndSettleTime(merchantId, settleTime);
//        verify(orderLocalAdapter).listOrdersByMerchantAndCreateTime(merchantId, yesterday, now);
//        verify(merchantLocalAdapter).listBalanceChangesByMerchantId(merchantId, yesterday, now);
//    }
//
//    @Test
//    void testExecuteDailySettlement_normal_withOrdersOnly() {
//        // Arrange
//        long merchantId = 1L;
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime yesterday = now.minusDays(1);
//        String settleTime = formatSettlementTime(now);
//
//        // Create test orders
//        OrderAmountInfoResponse order1 = new OrderAmountInfoResponse(1L, 1L, new BigDecimal("100.00"), null, null);
//        OrderAmountInfoResponse order2 = new OrderAmountInfoResponse(2L, 1L, new BigDecimal("50.00"), null, null);
//        List<OrderAmountInfoResponse> orders = Arrays.asList(order1, order2);
//
//        // Mock repository behavior for no existing settlement
//        when(settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime)).thenReturn(null);
//
//        // Mock adapter behavior with orders but no balance changes
//        when(orderLocalAdapter.listOrdersByMerchantAndCreateTime(merchantId, yesterday, now))
//                .thenReturn(orders);
//        when(merchantLocalAdapter.listBalanceChangesByMerchantId(merchantId, yesterday, now))
//                .thenReturn(List.of());
//
//        // Act
//        MerchantDailySettlement result = settlementService.executeDailySettlement(
//                merchantId, settleTime, yesterday, now);
//
//        // Assert
//        assertThat(result).isNotNull();
//        assertThat(result.getMerchantId()).isEqualTo(merchantId);
//
//        // 手动验证字段值
//        assertThat(result.getOrderAmount()).isEqualTo(new BigDecimal("150.00"));
//        assertThat(result.getMerchantIncreaseAmount()).isEqualTo(BigDecimal.ZERO);
//        assertThat(result.getDifferenceAmount()).isEqualTo(new BigDecimal("150.00"));
//
//        // Verify interactions
//        verify(settlementRepository).saveSettlement(result);
//        verify(settlementRepository).getByMerchantAndSettleTime(merchantId, settleTime);
//        verify(orderLocalAdapter).listOrdersByMerchantAndCreateTime(merchantId, yesterday, now);
//        verify(merchantLocalAdapter).listBalanceChangesByMerchantId(merchantId, yesterday, now);
//    }
//
//    @Test
//    void testExecuteDailySettlement_normal_withBalanceChangesOnly() {
//        // Arrange
//        long merchantId = 1L;
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime yesterday = now.minusDays(1);
//        String settleTime = formatSettlementTime(now);
//
//        // Create test balance changes
//        MerchantBalanceChangeResponse change1 = new MerchantBalanceChangeResponse(1L, 1L, new BigDecimal("1000.00"), null, null, null);
//        MerchantBalanceChangeResponse change2 = new MerchantBalanceChangeResponse(2L, 1L, null, new BigDecimal("1150.00"), null, null);
//        List<MerchantBalanceChangeResponse> balanceChanges = Arrays.asList(change1, change2);
//
//        // Mock repository behavior for no existing settlement
//        when(settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime)).thenReturn(null);
//
//        // Mock adapter behavior with balance changes but no orders
//        when(orderLocalAdapter.listOrdersByMerchantAndCreateTime(merchantId, yesterday, now))
//                .thenReturn(List.of());
//        when(merchantLocalAdapter.listBalanceChangesByMerchantId(merchantId, yesterday, now))
//                .thenReturn(balanceChanges);
//
//        // Act
//        MerchantDailySettlement result = settlementService.executeDailySettlement(
//                merchantId, settleTime, yesterday, now);
//
//        // Assert
//        assertThat(result).isNotNull();
//        assertThat(result.getMerchantId()).isEqualTo(merchantId);
//
//        // 手动验证字段값
//        assertThat(result.getOrderAmount()).isEqualTo(BigDecimal.ZERO);
//        assertThat(result.getMerchantIncreaseAmount()).isEqualTo(new BigDecimal("150.00"));
//        assertThat(result.getDifferenceAmount()).isEqualTo(new BigDecimal("-150.00"));
//
//        // Verify interactions
//        verify(settlementRepository).saveSettlement(result);
//        verify(settlementRepository).getByMerchantAndSettleTime(merchantId, settleTime);
//        verify(orderLocalAdapter).listOrdersByMerchantAndCreateTime(merchantId, yesterday, now);
//        verify(merchantLocalAdapter).listBalanceChangesByMerchantId(merchantId, yesterday, now);
//    }


}