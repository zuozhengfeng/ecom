package com.yabobaozb.ecom.order.infra.enums;

import lombok.Getter;

public enum OrderStatus {
    CREATED(0, "已创建"),
    PAID(1, "已支付"),
    CANCELED(2, "已取消"),
    REFUNDED(3, "已退款"),
    COMPLETED(4, "已完成");

    @Getter
    private final int code;
    @Getter
    private final String desc;

    OrderStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
