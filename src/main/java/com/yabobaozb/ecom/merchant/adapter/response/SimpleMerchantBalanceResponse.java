package com.yabobaozb.ecom.merchant.adapter.response;

import com.yabobaozb.ecom.merchant.domain.MerchantBalance;
import lombok.Getter;

import java.math.BigDecimal;

public record SimpleMerchantBalanceResponse(@Getter long merchantId, @Getter BigDecimal balance) {

    public static class Converter {
        public static SimpleMerchantBalanceResponse convert(MerchantBalance merchantBalance) {
            return new SimpleMerchantBalanceResponse(merchantBalance.getMerchantId(), merchantBalance.getBalance());
        }
    }

}
