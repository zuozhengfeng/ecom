package com.yabobaozb.ecom.merchant.infra.model;

import lombok.Getter;
import lombok.Setter;

public class MerchantBalanceVersionDO extends MerchantBalanceDO {

    @Getter @Setter
    private long oldVersion;
}
