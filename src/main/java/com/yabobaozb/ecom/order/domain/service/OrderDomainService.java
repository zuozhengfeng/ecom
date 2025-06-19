package com.yabobaozb.ecom.order.domain.service;

import com.yabobaozb.ecom.buyer.adapter.local.BuyerLocalAdapter;
import com.yabobaozb.ecom.buyer.adapter.response.SimpleBuyerBalanceResponse;
import com.yabobaozb.ecom.commodity.adapter.local.InventoryLocalAdapter;
import com.yabobaozb.ecom.commodity.adapter.response.SimpleSkuInfoResponse;
import com.yabobaozb.ecom.merchant.adapter.local.MerchantLocalAdapter;
import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.infra.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class OrderDomainService {

    private final IOrderRepository orderRepository;

    // 调用其他限界上下文的适配器
    private final InventoryLocalAdapter inventoryLocalAdapter;

    private final BuyerLocalAdapter buyerLocalAdapter;

    private final MerchantLocalAdapter merchantLocalAdapter;

    @Autowired
    public OrderDomainService(IOrderRepository orderRepository, InventoryLocalAdapter inventoryLocalAdapter, BuyerLocalAdapter buyerLocalAdapter, MerchantLocalAdapter merchantLocalAdapter) {
        this.orderRepository = orderRepository;
        this.inventoryLocalAdapter = inventoryLocalAdapter;
        this.buyerLocalAdapter = buyerLocalAdapter;
        this.merchantLocalAdapter = merchantLocalAdapter;
    }

    @Transactional
    public OrderInfo createOrder(long buyerId, long merchantId, long[] skuIds, int[] quantities, BigDecimal[] unitPrices, String remark) {
        // 构建订单
        OrderInfo orderInfo = buildOrder(buyerId, merchantId, skuIds, quantities, unitPrices, remark);
        orderInfo.create();

        // 存储订单，包括订单明细
        orderRepository.saveOrder(orderInfo);

        // 扣减余额
        SimpleBuyerBalanceResponse simpleBuyerBalanceResponse = buyerLocalAdapter.decreaseBalanceForPurchase(buyerId, orderInfo.getTotalAmount(), orderInfo.getOrderId());
        long payId = simpleBuyerBalanceResponse.payId();
        orderInfo.refreshToPaid(payId);

        // 更新订单状态
        orderRepository.refreshPayResult(orderInfo);

        // 扣减库存
        inventoryLocalAdapter.decreaseInventories(skuIds, quantities, remark);

        // 增加商家收益
        merchantLocalAdapter.increaseBalance(orderInfo.getMerchantId(), orderInfo.getTotalAmount(), orderInfo.getPayId(), orderInfo.getOrderId(), remark);

        // 发布创建订单事件
        publishOrderCreateEvent(orderInfo.getOrderId());

        return orderInfo;
    }

    private OrderInfo buildOrder(long buyerId, long merchantId, long[] skuIds, int[] quantities, BigDecimal[] unitPrices, String remark) {
        // 拿到商品库存
        Map<Long, SimpleSkuInfoResponse> skuInfos = inventoryLocalAdapter.listSkuInfos(merchantId, skuIds);

        // 拿到买家的余额
        SimpleBuyerBalanceResponse buyerBalance = buyerLocalAdapter.getByBuyerId(buyerId);

        return new OrderInfo(buyerId, merchantId, skuIds, quantities, unitPrices, remark, buyerBalance, skuInfos);
    }

    private void publishOrderCreateEvent(long orderId) {
        // TODO
    }
}
