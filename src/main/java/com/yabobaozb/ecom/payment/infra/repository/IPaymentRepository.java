package com.yabobaozb.ecom.payment.infra.repository;

import com.yabobaozb.ecom.payment.domain.Payment;

public interface IPaymentRepository {

    Payment addPayment(Payment payment);

    Payment getPayment(long payId);
}
