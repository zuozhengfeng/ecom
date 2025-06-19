package com.yabobaozb.ecom.commodity.infra.model.converter;

import com.yabobaozb.ecom.commodity.domain.SkuInventoryRecord;
import com.yabobaozb.ecom.commodity.infra.model.SkuInventoryRecordDO;

public final class SkuInventoryRecordConverter {
    public static SkuInventoryRecordDO convertToDO(SkuInventoryRecord skuInventoryRecord) {
        SkuInventoryRecordDO skuInventoryRecordDO = new SkuInventoryRecordDO();
        skuInventoryRecordDO.setRecordId(skuInventoryRecord.getRecordId());
        skuInventoryRecordDO.setSkuId(skuInventoryRecord.getSkuId());
        skuInventoryRecordDO.setChangeType((short)skuInventoryRecord.getChangeType().getValue());
        skuInventoryRecordDO.setQuantity(skuInventoryRecord.getQuantity());
        skuInventoryRecordDO.setInventoryFrom(skuInventoryRecord.getInventoryFrom());
        skuInventoryRecordDO.setInventoryTo(skuInventoryRecord.getInventoryTo());
        skuInventoryRecordDO.setPayId(skuInventoryRecord.getPayId());
        skuInventoryRecordDO.setSoruceOrderId(skuInventoryRecord.getSoruceOrderId());
        skuInventoryRecordDO.setRemark(skuInventoryRecord.getRemark());
        return skuInventoryRecordDO;
    }
}
