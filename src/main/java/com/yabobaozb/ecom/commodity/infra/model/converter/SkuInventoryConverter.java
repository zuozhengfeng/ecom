package com.yabobaozb.ecom.commodity.infra.model.converter;

import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import com.yabobaozb.ecom.commodity.infra.model.SkuInventoryDO;
import com.yabobaozb.ecom.commodity.infra.model.SkuInventoryVersionDO;

public final class SkuInventoryConverter {

    public static SkuInventoryDO convertToDO(SkuInventory skuInventory) {
        SkuInventoryDO skuInventoryDO = new SkuInventoryDO();
        skuInventoryDO.setSkuId(skuInventory.getSkuId());
        skuInventoryDO.setAvailableInventory(skuInventory.getAvailableInventory());
        skuInventoryDO.setVersion(skuInventory.getNewVersion());
        return skuInventoryDO;
    }

    public static SkuInventory convertToAggregate(SkuInventoryDO skuInventoryDO) {
        return new SkuInventory(skuInventoryDO.getSkuId(), skuInventoryDO.getAvailableInventory(), skuInventoryDO.getVersion());
    }


    public static SkuInventoryVersionDO convertToVersionDO(SkuInventory skuInventory) {
        SkuInventoryVersionDO skuInventoryVersionDO = new SkuInventoryVersionDO();
        skuInventoryVersionDO.setSkuId(skuInventory.getSkuId());
        skuInventoryVersionDO.setAvailableInventory(skuInventory.getAvailableInventory());
        skuInventoryVersionDO.setVersion(skuInventory.getNewVersion());
        skuInventoryVersionDO.setOldVersion(skuInventory.getVersion());
        return skuInventoryVersionDO;
    }
}
