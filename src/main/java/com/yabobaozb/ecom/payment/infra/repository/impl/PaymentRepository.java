package com.yabobaozb.ecom.payment.infra.repository.impl;

import com.yabobaozb.ecom.payment.domain.Payment;
import com.yabobaozb.ecom.payment.infra.mapper.PaymentInfoDOMapper;
import com.yabobaozb.ecom.payment.infra.model.PaymentInfoDO;
import com.yabobaozb.ecom.payment.infra.model.converter.PaymentConverter;
import com.yabobaozb.ecom.payment.infra.repository.IPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentRepository implements IPaymentRepository {

    private final PaymentInfoDOMapper paymentInfoDOMapper;

    @Autowired
    public PaymentRepository(PaymentInfoDOMapper paymentInfoDOMapper) {
        this.paymentInfoDOMapper = paymentInfoDOMapper;
    }


    @Override
    public Payment addPayment(Payment payment) {
        paymentInfoDOMapper.insertSelective(PaymentConverter.convertToDO(payment) );
        return payment;
    }

    @Override
    public Payment getPayment(long payId) {
        PaymentInfoDO paymentInfoDO = paymentInfoDOMapper.selectByPrimaryKey(payId);
        return PaymentConverter.convertToAggregate(paymentInfoDO);
    }

}
