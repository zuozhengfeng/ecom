package com.yabobaozb.ecom.buyer.infra.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

public class BuyerBalanceVersionDO extends BuyerBalanceDO {
    @Getter @Setter
    private long oldVersion;
}