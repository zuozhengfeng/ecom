package com.yabobaozb.ecom.commodity.infra.enums;

import lombok.Getter;

public enum SkuInventoryChangeType {

    MerchantIncrease(1),
    BuyerConsume(2);

    @Getter
    private final int value;

    SkuInventoryChangeType(int value) {
        this.value = value;
    }

    public static SkuInventoryChangeType getByValue(int value) {
        for (SkuInventoryChangeType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }

}
