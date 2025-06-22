package com.yabobaozb.ecom.settlement.infra.repository;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;

public interface ISettlementRepository {
    void saveSettlement(MerchantDailySettlement merchantDailySettlement);
}
