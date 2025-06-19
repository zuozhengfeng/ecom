package com.yabobaozb.ecom.order.infra.enums;

import lombok.Getter;

public enum PayResult {
    UNPAID(0, "未支付"),

    PAID(1, "已支付");

    @Getter
    private final int value;
    @Getter
    private final String desc;

    PayResult(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static PayResult parseByValue(int value) {
        for (PayResult payResult : PayResult.values()) {
            if (payResult.value == value) {
                return payResult;
            }
        }
        return null;
    }
}
