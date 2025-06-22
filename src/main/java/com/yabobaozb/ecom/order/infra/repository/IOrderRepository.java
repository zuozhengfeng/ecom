package com.yabobaozb.ecom.order.infra.repository;

import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.domain.OrderItem;
import com.yabobaozb.ecom.order.domain.OrderPayment;
import com.yabobaozb.ecom.order.infra.model.OrderInfoDO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository {

    void saveOrder(OrderInfo orderInfo);

    Optional<OrderInfo> getById(long orderId);

    List<OrderPayment> listPaymentsByOrderId(long orderId);

    List<OrderItem> listItemsByOrderId(long orderId);

    void refreshPayResult(OrderInfo orderInfo);

    List<OrderInfo> listByMerchantAndCreateTime(long merchantId, LocalDateTime beginAt, LocalDateTime endAt);
}
