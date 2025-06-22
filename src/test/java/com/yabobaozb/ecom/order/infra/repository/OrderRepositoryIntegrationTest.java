package com.yabobaozb.ecom.order.infra.repository;

import com.yabobaozb.ecom.buyer.adapter.local.BuyerLocalAdapter;
import com.yabobaozb.ecom.buyer.adapter.response.SimpleBuyerBalanceResponse;
import com.yabobaozb.ecom.commodity.adapter.local.CommodityLocalAdapter;
import com.yabobaozb.ecom.commodity.adapter.response.SimpleSkuInfoResponse;
import com.yabobaozb.ecom.order.domain.OrderInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderRepositoryIntegrationTest {

    private final IOrderRepository orderRepository;

    private final BuyerLocalAdapter buyerLocalAdapter;

    private final CommodityLocalAdapter commodityLocalAdapter;

    public OrderRepositoryIntegrationTest(IOrderRepository orderRepository, BuyerLocalAdapter buyerLocalAdapter, CommodityLocalAdapter commodityLocalAdapter) {
        this.orderRepository = orderRepository;
        this.buyerLocalAdapter = buyerLocalAdapter;
        this.commodityLocalAdapter = commodityLocalAdapter;
    }

    @Test
    public void testSaveOrder_Normal() {

        // 以下数据是根据data.sql准备的
        long buyerId = 10000L;
        long merchantId = 20000L;

        long[] skuIds = { 30000L, 30001L };
        int[] quantities = { 2, 3 };
        BigDecimal[] unitPrices = { new BigDecimal("1"), new BigDecimal("1") };
        String remark = "test order";

        SimpleBuyerBalanceResponse buyerBalance = buyerLocalAdapter.getByBuyerId(buyerId);
        Map<Long, SimpleSkuInfoResponse> skuInventories = commodityLocalAdapter.listSkuInfos(merchantId, skuIds);

        int skuSize = skuIds.length;
        int totalQuantity = Arrays.stream(quantities).sum();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for ( int i=0; i<skuSize; i++ ) {
            totalAmount = totalAmount.add(unitPrices[i].multiply(new BigDecimal(quantities[i])));
        }

        // 测试下面的代码
        OrderInfo orderInfo = new OrderInfo(buyerId, merchantId, skuIds, quantities, unitPrices, remark, buyerBalance, skuInventories);
        orderRepository.saveOrder(orderInfo);

        Optional<OrderInfo> orderInfoOptional = orderRepository.getById(orderInfo.getOrderId());
        assertTrue( orderInfoOptional.isPresent() );

        OrderInfo savedOrderInfo = orderInfoOptional.get();
        assertEquals( orderInfo.getOrderId(), savedOrderInfo.getOrderId() );
        assertEquals( remark, orderInfo.getRemark() );
        assertEquals( orderInfo.getBuyerId(), savedOrderInfo.getBuyerId() );
        assertEquals( orderInfo.getMerchantId(), savedOrderInfo.getMerchantId() );
        assertEquals( 0, totalAmount.compareTo(savedOrderInfo.getTotalAmount()) );
        assertEquals( skuSize, savedOrderInfo.getTotalSkuSize() );
        assertEquals( totalQuantity, savedOrderInfo.getTotalQuantity() );
        assertEquals( totalAmount, savedOrderInfo.getTotalAmount() );

    }

    @Test
    public void testRefreshPayResult_Normal() {
        // 以下数据是根据data.sql准备的
        long buyerId = 10000L;
        long merchantId = 20000L;

        long[] skuIds = { 30000L, 30001L };
        int[] quantities = { 2, 3 };
        BigDecimal[] unitPrices = { new BigDecimal("1"), new BigDecimal("1") };
        String remark = "test order";

        SimpleBuyerBalanceResponse buyerBalance = buyerLocalAdapter.getByBuyerId(buyerId);
        Map<Long, SimpleSkuInfoResponse> skuInventories = commodityLocalAdapter.listSkuInfos(merchantId, skuIds);

        int skuSize = skuIds.length;
        int totalQuantity = Arrays.stream(quantities).sum();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for ( int i=0; i<skuSize; i++ ) {
            totalAmount = totalAmount.add(unitPrices[i].multiply(new BigDecimal(quantities[i])));
        }

        // 测试下面的代码
        OrderInfo orderInfo = new OrderInfo(buyerId, merchantId, skuIds, quantities, unitPrices, remark, buyerBalance, skuInventories);
        orderRepository.saveOrder(orderInfo);

        SimpleBuyerBalanceResponse simpleBuyerBalanceResponse = buyerLocalAdapter.decreaseBalanceForPurchase(buyerId, orderInfo.getTotalAmount(), orderInfo.getOrderId());
        long payId = simpleBuyerBalanceResponse.payId();
        orderInfo.refreshToPaid(payId);

        // 假设前面的是成功的
        orderRepository.refreshPayResult(orderInfo);



    }





}
