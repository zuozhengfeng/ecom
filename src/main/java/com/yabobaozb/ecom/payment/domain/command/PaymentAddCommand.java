package com.yabobaozb.ecom.payment.domain.command;

import com.yabobaozb.ecom.common.ICommand;
import com.yabobaozb.ecom.payment.domain.Payment;
import com.yabobaozb.ecom.payment.domain.service.IPaymentDomainService;
import com.yabobaozb.ecom.payment.infra.enums.PayType;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class PaymentAddCommand implements ICommand<Payment> {

    @Getter
    private final long buyerId;

    @Getter
    private final BigDecimal amount;

    @Getter
    private final PayType payType;

    @Getter
    private final long orderId;

    @Getter
    private final String remark;

    private final IPaymentDomainService paymentDomainService;

    public PaymentAddCommand( long buyerId, BigDecimal amount, PayType payType, long orderId, String remark, IPaymentDomainService paymentDomainService) {
        this.buyerId = buyerId;
        this.amount = amount;
        this.payType = payType;
        this.orderId = orderId;
        this.remark = remark;
        this.paymentDomainService = paymentDomainService;
    }

    public Payment execute() {
        return paymentDomainService.addPayment(buyerId, amount, payType, orderId, remark);
    }

}
