package com.yabobaozb.ecom.settlement.infra.repository;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlementRecord;

public interface ISettlementRepository {
    void saveSettlement(MerchantDailySettlement merchantDailySettlement);
    MerchantDailySettlement getByMerchantAndSettleTime(long merchantId, String settleTime);

    MerchantDailySettlementRecord getRecentRecordByMerchantAndSettleTime(long merchantId, String settleTime);
}
