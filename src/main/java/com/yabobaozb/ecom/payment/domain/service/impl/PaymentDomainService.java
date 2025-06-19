package com.yabobaozb.ecom.payment.domain.service.impl;

import com.yabobaozb.ecom.payment.domain.Payment;
import com.yabobaozb.ecom.payment.domain.service.IPaymentDomainService;
import com.yabobaozb.ecom.payment.infra.enums.PaySource;
import com.yabobaozb.ecom.payment.infra.enums.PayType;
import com.yabobaozb.ecom.payment.infra.repository.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentDomainService implements IPaymentDomainService {

    private final IPaymentRepository paymentRepository;

    @Autowired
    public PaymentDomainService(IPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment getByPayId(long payId) {
        return paymentRepository.getPayment(payId);
    }

    @Override
    public Payment addPayment(long userId, BigDecimal amount, PayType payType, long orderId, String remark) {
        Payment payment = new Payment(userId, amount, payType, orderId, remark);
        return paymentRepository.addPayment(payment);
    }
}
