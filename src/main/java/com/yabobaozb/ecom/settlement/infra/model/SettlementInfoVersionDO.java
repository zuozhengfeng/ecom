package com.yabobaozb.ecom.settlement.infra.model;

import lombok.Getter;
import lombok.Setter;

public class SettlementInfoVersionDO extends SettlementInfoDO {
    @Getter @Setter
    private long oldVersion;
}
