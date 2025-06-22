package com.yabobaozb.ecom.merchant.adapter.response;

import com.yabobaozb.ecom.merchant.domain.MerchantBalanceRecord;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MerchantBalanceChangeResponse(@Getter long recordId, @Getter long merchantId, @Getter BigDecimal amount,
                                            @Getter BigDecimal balanceFrom, @Getter BigDecimal balanceTo,
                                            @Getter LocalDateTime createTime) {

    public static class Converter {
        public static MerchantBalanceChangeResponse convertToResponse(MerchantBalanceRecord merchantBalanceRecord) {
            return new MerchantBalanceChangeResponse(merchantBalanceRecord.getRecordId(), merchantBalanceRecord.getMerchantId(), merchantBalanceRecord.getAmount(), merchantBalanceRecord.getBalanceFrom(), merchantBalanceRecord.getBalanceTo(), merchantBalanceRecord.getCreateTime());
        }
    }

}
