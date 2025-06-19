package com.yabobaozb.ecom.order.infra.model.converter;

import com.yabobaozb.ecom.order.domain.OrderPayment;
import com.yabobaozb.ecom.order.infra.enums.PayResult;
import com.yabobaozb.ecom.order.infra.model.OrderPaymentDO;
import com.yabobaozb.ecom.payment.infra.enums.PayType;

public final class OrderPaymentConverter {

    public static OrderPaymentDO convertToDO(OrderPayment orderPayment) {
        OrderPaymentDO orderPaymentDO = new OrderPaymentDO();
        orderPaymentDO.setOrderId(orderPayment.getOrderId());
        orderPaymentDO.setPayId(orderPayment.getPayId());
        orderPaymentDO.setPayType((short)orderPayment.getPayType().getValue());
        orderPaymentDO.setAmount(orderPayment.getAmount());
        return orderPaymentDO;
    }

    public static OrderPayment convertToAggregate(OrderPaymentDO orderPaymentDO) {
        return new OrderPayment(orderPaymentDO.getOrderId(), orderPaymentDO.getPayId(), orderPaymentDO.getAmount(), PayType.parseByValue(orderPaymentDO.getPayType()), PayResult.PAID);
    }

}
