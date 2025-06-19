package com.yabobaozb.ecom.commodity.domain;


import com.yabobaozb.ecom.commodity.infra.enums.SkuInventoryChangeType;
import lombok.Getter;

public class SkuInventory {

    @Getter
    private final long skuId;

    @Getter
    private int availableInventory;

    @Getter
    private long newVersion;

    @Getter
    private final long version;

    @Getter
    private SkuInventoryRecord record;

    public SkuInventory(long skuId) {
        this(skuId, 0, -1);
    }

    public SkuInventory(long skuId, int availableInventory) {
        this(skuId, availableInventory, -1);
    }

    public SkuInventory(long skuId, int availableInventory, long version) {
        this.skuId = skuId;
        this.availableInventory = availableInventory;
        this.version = version;
    }

    public void increase(int quantity, SkuInventoryChangeType changeType, String remark) {
        if ( quantity <= 0 ) {
            throw new RuntimeException("数量必须大于0");
        }
        if ( null == changeType ) {
            throw new RuntimeException("未知的变动类型");
        }

        int inventoryFrom = this.availableInventory;
        int inventoryTo   = this.availableInventory + quantity;
        this.availableInventory = inventoryTo;

        this.record = new SkuInventoryRecord(this.skuId, changeType, quantity, inventoryFrom, inventoryTo, remark);
    }

    public void decrease(int quantity, SkuInventoryChangeType changeType, String remark) {
        if ( quantity <= 0 ) {
            throw new RuntimeException("数量必须大于0");
        }
        if ( null == changeType ) {
            throw new RuntimeException("未知的变动类型");
        }

        int inventoryFrom = this.availableInventory;
        int inventoryTo   = this.availableInventory - quantity;
        this.availableInventory = inventoryTo;

        if ( inventoryTo < 0 ) {
            throw new RuntimeException("库存不足");
        }

        this.record = new SkuInventoryRecord(this.skuId, changeType, quantity, inventoryFrom, inventoryTo, remark);
    }


    public void refreshVersion() {
        this.newVersion = 1 + this.version;
    }


}
