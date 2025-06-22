package com.yabobaozb.ecom.order.domain.service;

import com.yabobaozb.ecom.buyer.adapter.local.BuyerLocalAdapter;
import com.yabobaozb.ecom.buyer.adapter.response.SimpleBuyerBalanceResponse;
import com.yabobaozb.ecom.commodity.adapter.local.CommodityLocalAdapter;
import com.yabobaozb.ecom.commodity.adapter.response.SimpleSkuInfoResponse;
import com.yabobaozb.ecom.merchant.adapter.local.MerchantLocalAdapter;
import com.yabobaozb.ecom.merchant.adapter.response.SimpleMerchantBalanceResponse;
import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.domain.OrderItem;
import com.yabobaozb.ecom.order.domain.OrderPayment;
import com.yabobaozb.ecom.order.infra.enums.OrderStatus;
import com.yabobaozb.ecom.order.infra.repository.impl.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderDomainServiceIntegrationTest {

    private final OrderDomainService orderDomainService;

    private final OrderRepository orderRepository;

    private final BuyerLocalAdapter buyerLocalAdapter;

    private final MerchantLocalAdapter merchantLocalAdapter;

    private final CommodityLocalAdapter commodityLocalAdapter;


    @Autowired
    public OrderDomainServiceIntegrationTest(OrderDomainService orderDomainService, OrderRepository orderRepository, BuyerLocalAdapter buyerLocalAdapter, MerchantLocalAdapter merchantLocalAdapter, CommodityLocalAdapter commodityLocalAdapter) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.buyerLocalAdapter = buyerLocalAdapter;
        this.merchantLocalAdapter = merchantLocalAdapter;
        this.commodityLocalAdapter = commodityLocalAdapter;
    }

    @BeforeEach
    void setUp() {
    }
    
    @Test 
    void testCreateOrder_Successful() {
        // 准备测试数据
        // 说明
        // 买家10999的余额是100001.00元
        // 商家20000的余额是1000.00元
        // 商品30000的库存是99，售价是199.99元
        // 商品30999的库存是199，售价是301.19元
        // 本次创建的订单包含两个商品，商品30000的编号为1，商品30999的编号为2，总金额是199.99*2+301.19*3=1303.55元
        // 买家10999的余额将从100001.00变动到100001.00-1303.55=98697.45元
        // 商家20000的余额将从1000.00变动到1000.00+1303.55=2303.55元
        // 商品30000的库存将99变动到99-2=97
        // 商品30999的库存将199变动到199-3=196
        long buyerId = 10999L;
        long merchantId = 20000L;
        long[] skuIds = {30000L, 30999L};
        int[] quantities = {2, 3};
        BigDecimal[] unitPrices = {new BigDecimal("199.99"), new BigDecimal("301.19")};
        String remark = "Integration test order";

        BigDecimal totalPrice = BigDecimal.ZERO;
        for ( int i=0; i<skuIds.length; i++) {
            totalPrice = totalPrice.add( unitPrices[i].multiply(new BigDecimal(quantities[i])) );
        }

        // 执行测试
        // 执行测试
        // 执行测试
        OrderInfo result = orderDomainService.createOrder(buyerId, merchantId, skuIds, quantities, unitPrices, remark);
        assertNotNull(result);
        assertEquals(OrderStatus.PAID, result.getOrderStatus());
        assertTrue(result.getPayId() > 0 );
        assertEquals(0, result.getTotalAmount().compareTo(totalPrice));


        // 验证结果
        // 验证结果
        // 验证结果

        // 验证订单信息
        // 通过数据库验证订单是否成功保存
        Optional<OrderInfo> savedOrder = orderRepository.getById(result.getOrderId());
        assertTrue(savedOrder.isPresent());
        assertThat(savedOrder.get().getBuyerId()).isEqualTo(buyerId);
        assertThat(savedOrder.get().getMerchantId()).isEqualTo(merchantId);
        assertThat(savedOrder.get().getTotalAmount()).isEqualTo(result.getTotalAmount());
        assertNull(savedOrder.get().getOrderStatus());

        // 验证订单明细
        List<OrderItem> orderItems = orderRepository.listItemsByOrderId(result.getOrderId());
        assertThat(orderItems.size()).isEqualTo(skuIds.length);
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            assertThat(orderItem.getOrderId()).isEqualTo(result.getOrderId());
            assertThat(orderItem.getSkuId()).isEqualTo(skuIds[i]);
            assertThat(orderItem.getQuantity()).isEqualTo(quantities[i]);
            assertThat(orderItem.getUnitPrice()).isEqualTo(unitPrices[i]);
        }

        // 验证支付
        List<OrderPayment> orderPayments = orderRepository.listPaymentsByOrderId(result.getOrderId());
        assertThat(orderPayments).hasSize(1);
        assertThat(orderPayments.get(0).getOrderId()).isEqualTo(result.getOrderId());
        assertThat(orderPayments.get(0).getAmount()).isEqualTo(result.getTotalAmount());
        assertThat(orderPayments.get(0).getPayId()).isEqualTo(result.getPayId());

        // 验证用户余额
        SimpleBuyerBalanceResponse buyerBalance = buyerLocalAdapter.getByBuyerId(buyerId);
        assertEquals(0, buyerBalance.balance().compareTo(new BigDecimal("98697.45")));

        // 验证商家收益
        SimpleMerchantBalanceResponse merchantBalance = merchantLocalAdapter.getMerchantBalance(merchantId);
        assertEquals(0, merchantBalance.balance().compareTo(new BigDecimal("2303.55")));

        // 验证商品库存
        Map<Long, SimpleSkuInfoResponse> inventories = commodityLocalAdapter.listSkuInfos(merchantId, skuIds);
        SimpleSkuInfoResponse skuInfo0 = inventories.get(skuIds[0]);
        assertEquals(skuInfo0.getAvailableInventory(), 97);
        SimpleSkuInfoResponse skuInfo1 = inventories.get(skuIds[1]);
        assertEquals(skuInfo1.getAvailableInventory(), 196);


//        List<OrderPaymentDO> orderPayments = jdbcTemplate.query(
//        "select * from order_payment where pay_id = ?",
//            (rs, rowNum) -> {
//                OrderPaymentDO payment = new OrderPaymentDO();
//                payment.setPayId(rs.getLong("pay_id"));
//                payment.setOrderId(rs.getLong("order_id"));
//                payment.setPayType(rs.getShort("pay_type"));
//                payment.setAmount(rs.getBigDecimal("amount"));
//                return payment;
//            },
//            result.getOrderId()
//        );
//        assertThat(orderPayments).hasSize(1);
//        assertThat(orderPayments.get(0).getOrderId()).isEqualTo(result.getOrderId());
//        assertThat(orderPayments.get(0).getAmount()).isEqualTo(result.getTotalAmount());
//        assertThat(orderPayments.get(0).getPayId()).isEqualTo(result.getPayId());

    }

}
