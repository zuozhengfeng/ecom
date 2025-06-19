package com.yabobaozb.ecom.commodity.adapter.response;

import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SimpleSkuInventoryResponse {

    private final long skuId;

    private final int availableInventory;

    private final long version;

    public SimpleSkuInventoryResponse(long skuId, int availableInventory, long version) {
        this.skuId = skuId;
        this.availableInventory = availableInventory;
        this.version = version;
    }


    public final static class Converter {
        public static SimpleSkuInventoryResponse convert(SkuInventory skuInventory) {
            return new SimpleSkuInventoryResponse(skuInventory.getSkuId(), skuInventory.getAvailableInventory(), skuInventory.getNewVersion());
        }
    }

}
