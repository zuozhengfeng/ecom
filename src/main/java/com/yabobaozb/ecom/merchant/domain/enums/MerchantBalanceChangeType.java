package com.yabobaozb.ecom.merchant.domain.enums;

import lombok.Getter;

public enum MerchantBalanceChangeType {

    fromSell(1, "销售商品");

    @Getter
    private final int value;

    @Getter
    private final String desc;

    MerchantBalanceChangeType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static MerchantBalanceChangeType getByValue(int value) {
        for (MerchantBalanceChangeType type : MerchantBalanceChangeType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }


}
