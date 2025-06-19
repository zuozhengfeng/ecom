package com.yabobaozb.ecom.commodity.domain;

import com.yabobaozb.ecom.commodity.infra.enums.SkuInventoryChangeType;
import com.yabobaozb.ecom.common.infra.util.IDGenerator;
import lombok.Data;

@Data
public class SkuInventoryRecord {
    private long recordId;

    private final long skuId;

    private final SkuInventoryChangeType changeType;

    private int quantity;


    private int inventoryFrom;


    private int inventoryTo;


    private long payId;


    private long soruceOrderId;

    private String remark;

    public SkuInventoryRecord(long skuId, SkuInventoryChangeType changeType,
                            int quantity, int inventoryFrom, int inventoryTo, String remark) {
        this.skuId = skuId;
        this.changeType = changeType;
        this.quantity = quantity;
        this.inventoryFrom = inventoryFrom;
        this.inventoryTo = inventoryTo;
        this.remark = remark;

        this.recordId = generateId();
    }

    private long generateId() {
        return IDGenerator.generateId();
    }


}
