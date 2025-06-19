package com.yabobaozb.ecom.commodity.infra.model;

import lombok.Getter;
import lombok.Setter;

public class SkuInventoryVersionDO extends SkuInventoryDO {

    @Getter @Setter
    private long oldVersion;

}