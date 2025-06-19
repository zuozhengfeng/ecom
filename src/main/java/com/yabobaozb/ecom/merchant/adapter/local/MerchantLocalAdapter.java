package com.yabobaozb.ecom.merchant.adapter.local;

import com.yabobaozb.ecom.merchant.adapter.response.SimpleMerchantBalanceResponse;
import com.yabobaozb.ecom.merchant.domain.command.MerchantBlanaceDecreaseCommand;
import com.yabobaozb.ecom.merchant.domain.command.MerchantBlanaceIncreaseCommand;
import com.yabobaozb.ecom.merchant.domain.service.MerchantBalanceDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MerchantLocalAdapter {

    private final MerchantBalanceDomainService merchantBalanceDomainService;

    @Autowired
    public MerchantLocalAdapter(MerchantBalanceDomainService merchantBalanceDomainService) {
        this.merchantBalanceDomainService = merchantBalanceDomainService;
    }

    public SimpleMerchantBalanceResponse getMerchantBalance(long merchantId) {
        return SimpleMerchantBalanceResponse.Converter.convert(merchantBalanceDomainService.getMerchantBalance(merchantId));
    }

    public void increaseBalance(long merchantId, BigDecimal amount, long payId, long sourceOrderId, String remark) {
        MerchantBlanaceIncreaseCommand command = new MerchantBlanaceIncreaseCommand(merchantId, amount, payId, sourceOrderId, remark, this.merchantBalanceDomainService);
        command.execute();
    }

    public void decreaseBalance(long merchantId, BigDecimal amount, String remark) {
        MerchantBlanaceDecreaseCommand command = new MerchantBlanaceDecreaseCommand(merchantId, amount, remark, this.merchantBalanceDomainService);
        command.execute();
    }

}
