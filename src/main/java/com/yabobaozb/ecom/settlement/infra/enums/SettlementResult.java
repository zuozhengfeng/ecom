package com.yabobaozb.ecom.settlement.infra.enums;

import lombok.Getter;

public enum SettlementResult {

    MATCHED(1, "结算正常"),
    NOTMATCHED(2, "结算异常");

    @Getter
    private final int value;

    @Getter
    private final String desc;

    SettlementResult(int value, String desc) {
        this.value = value;
        this.desc = desc;
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
