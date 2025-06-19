package com.yabobaozb.ecom.commodity.domain;

import lombok.Getter;

import java.math.BigDecimal;

public class SkuInfo {

    @Getter
    private final long skuId;

    @Getter
    private final String name;

    @Getter
    private final BigDecimal unitPrice;

    @Getter
    private final long merchantId;

    @Getter
    private final SkuInventory skuInventory;

    public SkuInfo(long skuId, String name, BigDecimal unitPrice, long merchantId, SkuInventory skuInventory) {
        this.skuId = skuId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.merchantId = merchantId;
        this.skuInventory = skuInventory;
    }

}
