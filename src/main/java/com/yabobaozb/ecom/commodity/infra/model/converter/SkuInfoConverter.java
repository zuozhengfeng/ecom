package com.yabobaozb.ecom.commodity.infra.model.converter;

import com.yabobaozb.ecom.commodity.domain.SkuInfo;
import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import com.yabobaozb.ecom.commodity.infra.model.SkuInfoDO;
import com.yabobaozb.ecom.commodity.infra.model.SkuInventoryDO;

public final class SkuInfoConverter {

    public static SkuInfo convertToAggregate(SkuInfoDO skuInfoDO, SkuInventoryDO skuInventoryDO) {
        if ( null != skuInventoryDO ) {
            return new SkuInfo(skuInfoDO.getSkuId(), skuInfoDO.getName(), skuInfoDO.getSalePrice(), skuInfoDO.getMerchantId(), SkuInventoryConverter.convertToAggregate(skuInventoryDO));
        }
        else {
            return new SkuInfo(skuInfoDO.getSkuId(), skuInfoDO.getName(), skuInfoDO.getSalePrice(), skuInfoDO.getMerchantId(), new SkuInventory(skuInfoDO.getSkuId()));
        }
    }

}
