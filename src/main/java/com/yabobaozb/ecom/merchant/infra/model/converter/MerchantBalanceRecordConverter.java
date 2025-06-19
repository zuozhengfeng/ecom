package com.yabobaozb.ecom.merchant.infra.model.converter;

import com.yabobaozb.ecom.merchant.domain.MerchantBalanceRecord;
import com.yabobaozb.ecom.merchant.infra.model.MerchantBalanceRecordDO;

public final class MerchantBalanceRecordConverter {

    public static MerchantBalanceRecord convertToAggregate(MerchantBalanceRecordDO recordDO) {
        return new MerchantBalanceRecord(recordDO.getRecordId(),
            recordDO.getMerchantId(),
            recordDO.getAmount(), recordDO.getBalanceFrom(), recordDO.getBalanceTo(),
            recordDO.getChangeType(),
            recordDO.getPayId(), recordDO.getSourceOrderId(),
            recordDO.getRemark());
    }

    public static MerchantBalanceRecordDO convertToDO(MerchantBalanceRecord merchantBalanceRecord) {
        MerchantBalanceRecordDO merchantBalanceRecordDO = new MerchantBalanceRecordDO();
        merchantBalanceRecordDO.setRecordId(merchantBalanceRecord.getRecordId());
        merchantBalanceRecordDO.setMerchantId(merchantBalanceRecord.getMerchantId());
        merchantBalanceRecordDO.setAmount(merchantBalanceRecord.getAmount());
        merchantBalanceRecordDO.setBalanceFrom(merchantBalanceRecord.getBalanceFrom());
        merchantBalanceRecordDO.setBalanceTo(merchantBalanceRecord.getBalanceTo());
        merchantBalanceRecordDO.setChangeType((short) merchantBalanceRecord.getChangeType().getValue());
        merchantBalanceRecordDO.setPayId(merchantBalanceRecord.getPayId());
        merchantBalanceRecordDO.setRemark(merchantBalanceRecord.getRemark());
        merchantBalanceRecordDO.setSourceOrderId(merchantBalanceRecord.getSourceOrderId());
        return merchantBalanceRecordDO;
    }

}
