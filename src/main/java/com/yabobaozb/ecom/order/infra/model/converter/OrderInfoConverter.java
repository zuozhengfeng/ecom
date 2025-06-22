package com.yabobaozb.ecom.order.infra.model.converter;

import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.infra.model.OrderInfoDO;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class OrderInfoConverter {

    public static OrderInfoDO convertToDO(OrderInfo orderInfo) {
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setOrderId(orderInfo.getOrderId());
        orderInfoDO.setAmountPayable(orderInfo.getTotalAmount());
        orderInfoDO.setAmountPaid(orderInfo.getTotalAmount());
        orderInfoDO.setSkuSize(orderInfo.getTotalSkuSize());
        orderInfoDO.setTotalQuantity(orderInfo.getTotalQuantity());
        orderInfoDO.setBuyerId(orderInfo.getBuyerId());
        orderInfoDO.setMerchantId(orderInfo.getMerchantId());
        orderInfoDO.setOrderStatus((short)orderInfo.getOrderStatus().getCode());
        orderInfoDO.setPayResult((short)orderInfo.getPayResult().getValue());
        orderInfoDO.setRemark(orderInfo.getRemark());
        return orderInfoDO;
    }

    public static OrderInfoDO convertToDOOnlyOrderStatus(OrderInfo orderInfo) {
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setOrderId(orderInfo.getOrderId());
        orderInfoDO.setOrderStatus((short)orderInfo.getOrderStatus().getCode());
        return orderInfoDO;
    }

    public static OrderInfo convertToAggregate(OrderInfoDO orderInfoDO) {
        LocalDateTime createTime = orderInfoDO.getCreateTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return new OrderInfo( orderInfoDO.getOrderId(), orderInfoDO.getBuyerId(), orderInfoDO.getMerchantId(), orderInfoDO.getAmountPaid(), createTime );
    }
}
