package com.yabobaozb.ecom.settlement.infra.enums;

import lombok.Getter;

public enum SettlementResult {

    MATCHED(1),
    NOTMATCHED(2);

    @Getter
    private final int value;

    SettlementResult(int value) {
        this.value = value;
    }

    public static SettlementResult parseByValue(int value) {
        for (SettlementResult settlementResult : SettlementResult.values()) {
            if (settlementResult.value == value) {
                return settlementResult;
            }
        }
        return null;
    }

}
