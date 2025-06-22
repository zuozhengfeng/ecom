package com.yabobaozb.ecom.settlement.infra.enums;

import lombok.Getter;

public enum SettlementPeriod {

    DAILY(1);

    @Getter
    private final int value;

    SettlementPeriod(int value) {
        this.value = value;
    }

    public static SettlementPeriod parseByValue(int value) {
        for (SettlementPeriod period : SettlementPeriod.values()) {
            if (period.value == value) {
                return period;
            }
        }
        return null;
    }
}
