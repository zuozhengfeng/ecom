package com.yabobaozb.ecom.order.domain;

import com.yabobaozb.ecom.common.infra.util.IDGenerator;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class OrderItem {

    @Getter @Setter
    private long itemId;

    @Getter @Setter
    private long orderId;

    private final long skuId;
    private final String skuName;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderItem(long orderId, long skuId, String skuName, int quantity, BigDecimal unitPrice) {
        this(-1L, orderId, skuId, skuName, quantity, unitPrice);
    }

    public OrderItem(long itemId, long orderId, long skuId, String skuName, int quantity, BigDecimal unitPrice) {
        this.orderId = orderId;
        this.skuId = skuId;
        this.skuName = skuName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;

        this.itemId = itemId>0 ? itemId : generateItemId();
    }

    private long generateItemId() {
        return IDGenerator.generateId();
    }

}
