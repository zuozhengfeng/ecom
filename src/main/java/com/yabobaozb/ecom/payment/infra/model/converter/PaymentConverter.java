package com.yabobaozb.ecom.payment.infra.model.converter;

import com.yabobaozb.ecom.payment.domain.Payment;
import com.yabobaozb.ecom.payment.infra.enums.PayType;
import com.yabobaozb.ecom.payment.infra.model.PaymentInfoDO;

public final class PaymentConverter {

    public static PaymentInfoDO convertToDO(Payment payment) {
        PaymentInfoDO paymentInfoDO = new PaymentInfoDO();
        paymentInfoDO.setPayId( payment.getPayId() );
        paymentInfoDO.setAmount( payment.getAmount() );
        paymentInfoDO.setPayType( (short)payment.getPayType().getValue() );
        paymentInfoDO.setSourceOrderId( payment.getSourceOrderId() );
        paymentInfoDO.setRemark( payment.getRemark() );
        return paymentInfoDO;
    }

    public static Payment convertToAggregate(PaymentInfoDO paymentInfoDO) {
        return new Payment(
            paymentInfoDO.getPayId(),
            paymentInfoDO.getAmount(),
            PayType.parseByValue(paymentInfoDO.getPayType()),
            paymentInfoDO.getSourceOrderId(),
            paymentInfoDO.getRemark());
    }
}
