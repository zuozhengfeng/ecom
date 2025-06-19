package com.yabobaozb.ecom.buyer.adapter.response;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import lombok.Getter;

import java.math.BigDecimal;


public record SimpleBuyerBalanceResponse(@Getter long buyerId, @Getter BigDecimal balance, @Getter long payId,
                                         @Getter long version) {

    public final static class Converter {
        public static SimpleBuyerBalanceResponse convert(BuyerBalance buyerBalance) {
            return new SimpleBuyerBalanceResponse(buyerBalance.getBuyerId(), buyerBalance.getBalance(), buyerBalance.getPayId(), buyerBalance.getNewVersion());
        }
    }
}
