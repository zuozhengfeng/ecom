package com.yabobaozb.ecom.order.infra.model.converter;

import com.yabobaozb.ecom.order.domain.OrderItem;
import com.yabobaozb.ecom.order.infra.model.OrderItemDO;

public final class OrderItemConverter {

    public static OrderItemDO convertToDO(OrderItem orderItem) {
        OrderItemDO orderItemDO = new OrderItemDO();
        orderItemDO.setItemId(orderItem.getItemId());
        orderItemDO.setOrderId(orderItem.getOrderId());
        orderItemDO.setSkuId(orderItem.getSkuId());
        orderItemDO.setSkuName(orderItem.getSkuName());
        orderItemDO.setQuantity(orderItem.getQuantity());
        orderItemDO.setUnitPrice(orderItem.getUnitPrice());
        return orderItemDO;
    }

    public static OrderItem convertToAggregate(OrderItemDO orderItemDO) {
        return new OrderItem(orderItemDO.getItemId(), orderItemDO.getOrderId(), orderItemDO.getSkuId(), orderItemDO.getSkuName(), orderItemDO.getQuantity(), orderItemDO.getUnitPrice());
    }
}
