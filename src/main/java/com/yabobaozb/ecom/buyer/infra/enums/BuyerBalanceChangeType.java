package com.yabobaozb.ecom.buyer.infra.enums;

import lombok.Getter;

public enum BuyerBalanceChangeType {

    Recharge(1),
    Purchase(2);

    @Getter
    private final int value;

    BuyerBalanceChangeType(int value) {
        this.value = value;
    }


}
