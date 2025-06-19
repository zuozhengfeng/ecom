package com.yabobaozb.ecom.commodity.domain.command;

import com.yabobaozb.ecom.commodity.domain.service.InventoryDomainService;
import com.yabobaozb.ecom.common.ICommand;
import com.yabobaozb.ecom.commodity.domain.SkuInventory;

public class InventoryDecreaseCommand implements ICommand<SkuInventory> {

    private final long skuId;
    private final int quantity;
    private final String remark;
    private final InventoryDomainService inventoryDomainService;


    public InventoryDecreaseCommand(long skuId, int quantity, String remark, InventoryDomainService inventoryDomainService) {
        this.skuId = skuId;
        this.quantity = quantity;
        this.remark = remark;
        this.inventoryDomainService = inventoryDomainService;
    }

    @Override
    public SkuInventory execute() {
        return inventoryDomainService.decreaseInventory(skuId, quantity,  remark);
    }
}
