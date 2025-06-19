package com.yabobaozb.ecom.payment.domain;

import com.yabobaozb.ecom.common.infra.util.IDGenerator;
import com.yabobaozb.ecom.payment.infra.enums.PayType;
import lombok.Getter;

import java.math.BigDecimal;

public class Payment {

    @Getter
    private long payId;

    @Getter
    private final long buyerId;

    @Getter
    private final BigDecimal amount;

    @Getter
    private final PayType payType;

    @Getter
    private final long sourceOrderId;

    @Getter
    private final String remark;

    public Payment(long buyerId, BigDecimal amount, PayType payType, long sourceOrderId, String remark) {
        this.buyerId = buyerId;
        this.amount = amount;
        this.payType = payType;
        this.sourceOrderId = sourceOrderId;
        this.remark = remark;

        this.payId = generatePayId();
    }

    private long generatePayId() {
        return IDGenerator.generateId();
    }

}
