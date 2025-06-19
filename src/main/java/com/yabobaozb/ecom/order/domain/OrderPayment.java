package com.yabobaozb.ecom.order.domain;

import com.yabobaozb.ecom.order.infra.enums.PayResult;
import com.yabobaozb.ecom.payment.infra.enums.PayType;
import lombok.Getter;

import java.math.BigDecimal;

public class OrderPayment {

    @Getter
    private final long orderId;
    @Getter
    private final long payId;
    @Getter
    private final BigDecimal amount;
    @Getter
    private final PayType payType;
    @Getter
    private final PayResult payResult;

    public OrderPayment(long orderId, long payId, BigDecimal amount, PayType payType, PayResult payResult) {
        this.orderId = orderId;
        this.payId = payId;
        this.amount = amount;
        this.payType = payType;
        this.payResult = payResult;
    }

}
