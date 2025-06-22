package com.yabobaozb.ecom.merchant.infra.repository;

import com.yabobaozb.ecom.merchant.domain.MerchantBalance;
import com.yabobaozb.ecom.merchant.domain.MerchantBalanceRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface IMerchantBalanceRepository {

    void refreshBalance(MerchantBalance merchantBalance);

    MerchantBalance getByMerchantId(long merchantId);

    List<MerchantBalanceRecord> getRecordsByMerchantId(long merchantId);

    List<Long> listAllValidMerchantIds();

    List<MerchantBalanceRecord> selectByMerchantAndCreateTime(long merchantId, LocalDateTime beginAt, LocalDateTime endAt);
}
