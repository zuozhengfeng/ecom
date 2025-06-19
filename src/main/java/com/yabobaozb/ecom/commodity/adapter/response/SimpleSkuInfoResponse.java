package com.yabobaozb.ecom.commodity.adapter.response;

import com.yabobaozb.ecom.commodity.domain.SkuInfo;
import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SimpleSkuInfoResponse {

    private final long skuId;

    private final String skuName;

    private final BigDecimal unitPrice;

    private final int availableInventory;

    private final long version;

    public SimpleSkuInfoResponse(long skuId, String skuName, BigDecimal unitPrice, int availableInventory, long version) {
        this.skuId = skuId;
        this.skuName = skuName;
        this.unitPrice = unitPrice;
        this.availableInventory = availableInventory;
        this.version = version;
    }


    public final static class Converter {
        public static SimpleSkuInfoResponse convert(SkuInfo skuInfo) {
            return new SimpleSkuInfoResponse(skuInfo.getSkuId(), skuInfo.getName(), skuInfo.getUnitPrice(), skuInfo.getSkuInventory().getAvailableInventory(), skuInfo.getSkuInventory().getNewVersion());
        }
    }

}
