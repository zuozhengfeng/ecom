package com.yabobaozb.ecom.payment.adapter.local.vo;

import com.yabobaozb.ecom.payment.domain.Payment;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SimplePaymentVO {

    private long payId;

    private long buyerId;

    private BigDecimal amount;


    public final static class SimplePaymentVOConverter {
        public static SimplePaymentVO convert(Payment payment) {
            SimplePaymentVO simplePaymentVO = new SimplePaymentVO();
            simplePaymentVO.setPayId(payment.getPayId());
            simplePaymentVO.setBuyerId(payment.getBuyerId());
            simplePaymentVO.setAmount(payment.getAmount());
            return simplePaymentVO;
        }
    }
}
