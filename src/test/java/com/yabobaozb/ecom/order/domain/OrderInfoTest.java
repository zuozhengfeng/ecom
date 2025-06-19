package com.yabobaozb.ecom.order.domain;

import com.yabobaozb.ecom.buyer.adapter.response.SimpleBuyerBalanceResponse;
import com.yabobaozb.ecom.commodity.adapter.response.SimpleSkuInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderInfoTest {
    @BeforeEach
    void setUp() {

    }

    @Test
    void testCheckOrderValid_success() {
        // 创建订单对象
        long buyerId = 1L;
        long merchantId = 100L;
        long[] skuIds = {1000L};
        int[] quantities = {2};
        BigDecimal[] unitPrices = {new BigDecimal("50.00")};
        SimpleBuyerBalanceResponse buyerBalance = new SimpleBuyerBalanceResponse(buyerId, new BigDecimal("100.00"), 1L, 1L);
        Map<Long, SimpleSkuInfoResponse> skuInventories = new HashMap<>();
        skuInventories.put(1000L, new SimpleSkuInfoResponse(
                1000L, "-", new BigDecimal("50.00"), 10, 1L));
        OrderInfo orderInfo = new OrderInfo(buyerId, merchantId, skuIds, quantities, unitPrices, "test", buyerBalance, skuInventories);

        // 校验通过
        assertDoesNotThrow(orderInfo::checkOrderValid);
    }

    // 用户余额不足
    @Test
    void testCheckOrderValid_InsufficientBuyerBalance() {
        // 创建订单对象
        long buyerId = 1L;
        long merchantId = 100L;
        long[] skuIds = {1000L};
        int[] quantities = {2};
        BigDecimal[] unitPrices = {new BigDecimal("50.00")};
        SimpleBuyerBalanceResponse buyerBalance = new SimpleBuyerBalanceResponse(buyerId, new BigDecimal("99.00"), 1L, 1L);
        Map<Long, SimpleSkuInfoResponse> skuInventories = new HashMap<>();
        skuInventories.put(1000L, new SimpleSkuInfoResponse(
                1000L, "-", new BigDecimal("50.00"), 10, 1L));
        OrderInfo orderInfo = new OrderInfo(buyerId, merchantId, skuIds, quantities, unitPrices, "test", buyerBalance, skuInventories);

        // 应该抛出余额不足异常
        Exception exception = assertThrows(RuntimeException.class, orderInfo::checkOrderValid);
        assertEquals("用户余额不足", exception.getMessage());
    }

    // 库存不足
    @Test
    void testCheckOrderValid_InsufficientSkuStock() {

        // 创建订单对象
        long buyerId = 1L;
        long merchantId = 100L;
        long[] skuIds = {1000L};
        int[] quantities = {2};
        BigDecimal[] unitPrices = {new BigDecimal("50.00")};
        SimpleBuyerBalanceResponse buyerBalance = new SimpleBuyerBalanceResponse(buyerId, new BigDecimal("100.00"), 1L, 1L);
        Map<Long, SimpleSkuInfoResponse> skuInventories = new HashMap<>();
        skuInventories.put(1000L, new SimpleSkuInfoResponse(
                1000L, "-", new BigDecimal("50.00"), 1, 1L));
        OrderInfo orderInfo = new OrderInfo(buyerId, merchantId, skuIds, quantities, unitPrices, "test", buyerBalance, skuInventories);

        // 应该抛出库存不足异常
        Exception exception = assertThrows(RuntimeException.class, orderInfo::checkOrderValid);

        assertEquals("商品库存不足", exception.getMessage());
    }

    @Test
    void testCheckOrderValid_MismatchSkuPrice() {
        // 创建订单对象
        long buyerId = 1L;
        long merchantId = 100L;
        long[] skuIds = {1000L};
        int[] quantities = {2};
        BigDecimal[] unitPrices = {new BigDecimal("49.00")};
        SimpleBuyerBalanceResponse buyerBalance = new SimpleBuyerBalanceResponse(buyerId, new BigDecimal("100.00"), 1L, 1L);
        Map<Long, SimpleSkuInfoResponse> skuInventories = new HashMap<>();
        skuInventories.put(1000L, new SimpleSkuInfoResponse(
                1000L, "-", new BigDecimal("50.00"), 10, 1L));
        OrderInfo orderInfo = new OrderInfo(buyerId, merchantId, skuIds, quantities, unitPrices, "test", buyerBalance, skuInventories);


        // 应该抛出价格不一致异常
        Exception exception = assertThrows(RuntimeException.class, orderInfo::checkOrderValid);

        assertEquals("商品价格不一致", exception.getMessage());
    }

}
