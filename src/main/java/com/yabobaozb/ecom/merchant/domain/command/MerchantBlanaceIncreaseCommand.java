package com.yabobaozb.ecom.merchant.domain.command;

import com.yabobaozb.ecom.common.ICommand;
import com.yabobaozb.ecom.merchant.domain.MerchantBalance;
import com.yabobaozb.ecom.merchant.domain.service.MerchantBalanceDomainService;

import java.math.BigDecimal;

public class MerchantBlanaceIncreaseCommand implements ICommand<MerchantBalance> {
    private final long merchantId;
    private final BigDecimal amount;
    private final long payId;

    private final long sourceOrderId;
    private final String remark;

    private final MerchantBalanceDomainService merchantBalanceDomainService;

    public MerchantBlanaceIncreaseCommand(long merchantId, BigDecimal amount, long payId, long sourceOrderId, String remark, MerchantBalanceDomainService merchantBalanceDomainService) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.payId = payId;
        this.sourceOrderId = sourceOrderId;
        this.remark = remark;
        this.merchantBalanceDomainService = merchantBalanceDomainService;
    }

    @Override
    public MerchantBalance execute() {
        return merchantBalanceDomainService.increaseBalance(merchantId, amount, payId, sourceOrderId, remark);
    }
}
