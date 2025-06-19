package com.yabobaozb.ecom.payment.domain.service;

import com.yabobaozb.ecom.payment.domain.Payment;
import com.yabobaozb.ecom.payment.infra.enums.PayType;

import java.math.BigDecimal;

public interface IPaymentDomainService {

    Payment getByPayId(long payId);

    Payment addPayment(long userId, BigDecimal amount, PayType payType, long orderId, String remark);
}
