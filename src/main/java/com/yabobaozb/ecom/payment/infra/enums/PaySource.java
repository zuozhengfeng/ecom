package com.yabobaozb.ecom.payment.infra.enums;

import lombok.Getter;

public enum PaySource {

    RECHARGE(1),
    CONSUME(2);

    @Getter
    private final int value;

    PaySource(int value) {
        this.value = value;
    }

}
