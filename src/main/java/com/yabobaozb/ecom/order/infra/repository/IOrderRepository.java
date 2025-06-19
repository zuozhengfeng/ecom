package com.yabobaozb.ecom.order.infra.repository;

import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.domain.OrderItem;
import com.yabobaozb.ecom.order.domain.OrderPayment;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository {

    void saveOrder(OrderInfo orderInfo);

    Optional<OrderInfo> getById(long orderId);

    List<OrderPayment> listPaymentsByOrderId(long orderId);

    List<OrderItem> listItemsByOrderId(long orderId);

    void refreshPayResult(OrderInfo orderInfo);
}
