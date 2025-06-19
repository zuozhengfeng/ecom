package com.yabobaozb.ecom.payment.adapter.local;

import com.yabobaozb.ecom.payment.adapter.local.vo.SimplePaymentVO;
import com.yabobaozb.ecom.payment.domain.Payment;
import com.yabobaozb.ecom.payment.domain.command.PaymentAddCommand;
import com.yabobaozb.ecom.payment.domain.service.IPaymentDomainService;
import com.yabobaozb.ecom.payment.infra.enums.PayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentLocalAdapter {

    @Autowired
    private IPaymentDomainService paymentDomainService;

    public SimplePaymentVO getByPayId(long payId) {
        Payment payment = paymentDomainService.getByPayId(payId);
        return SimplePaymentVO.SimplePaymentVOConverter.convert(payment);
    }

    public long addPayment(long buyerId, BigDecimal amount, PayType payType, long orderId, String remark) {
        PaymentAddCommand command = new PaymentAddCommand(buyerId, amount, payType, orderId, remark, paymentDomainService);
        return command.execute().getPayId();
    }
}
