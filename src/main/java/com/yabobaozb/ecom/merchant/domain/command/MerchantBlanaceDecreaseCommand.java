package com.yabobaozb.ecom.merchant.domain.command;

import com.yabobaozb.ecom.common.ICommand;
import com.yabobaozb.ecom.merchant.domain.MerchantBalance;
import com.yabobaozb.ecom.merchant.domain.service.MerchantBalanceDomainService;

import java.math.BigDecimal;

public class MerchantBlanaceDecreaseCommand implements ICommand<MerchantBalance> {
    private final long merchantId;
    private final BigDecimal amount;
    private final String remark;

    private final MerchantBalanceDomainService merchantBalanceDomainService;

    public MerchantBlanaceDecreaseCommand(long merchantId, BigDecimal amount, String remark, MerchantBalanceDomainService merchantBalanceDomainService) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.remark = remark;
        this.merchantBalanceDomainService = merchantBalanceDomainService;
    }

    @Override
    public MerchantBalance execute() {
        return merchantBalanceDomainService.decreaseBalance(merchantId, amount, remark);
    }
}
