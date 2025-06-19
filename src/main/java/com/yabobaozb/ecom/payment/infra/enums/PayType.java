package com.yabobaozb.ecom.payment.infra.enums;

import lombok.Getter;

public enum PayType {

    CASH(1),
    BALANCE(2);

    @Getter
    private final int value;

    PayType(int value) {
        this.value = value;
    }

    public static PayType parseByValue(int value) {
        for (PayType payType : PayType.values()) {
            if (payType.value == value) {
                return payType;
            }
        }
        return null;
    }
}
