package com.yabobaozb.ecom.buyer.infra.model.converter;

import com.yabobaozb.ecom.buyer.domain.BuyerBalanceRecord;
import com.yabobaozb.ecom.buyer.infra.model.BuyerBalanceRecordDO;

public final class BuyerBalanceRecordConverter {

    public static BuyerBalanceRecordDO convertToDO(BuyerBalanceRecord buyerBalanceRecord) {
        BuyerBalanceRecordDO buyerBalanceRecordDO = new BuyerBalanceRecordDO();
        buyerBalanceRecordDO.setRecordId(buyerBalanceRecord.getRecordId());
        buyerBalanceRecordDO.setBuyerId(buyerBalanceRecord.getBuyerId());
        buyerBalanceRecordDO.setAmount(buyerBalanceRecord.getAmount());
        buyerBalanceRecordDO.setBalanceFrom(buyerBalanceRecord.getBalanceFrom());
        buyerBalanceRecordDO.setBalanceTo(buyerBalanceRecord.getBalanceTo());
        buyerBalanceRecordDO.setChangeType((short) buyerBalanceRecord.getBuyerBalanceChangeType().getValue());
        buyerBalanceRecordDO.setPayId(buyerBalanceRecord.getPayId());
        buyerBalanceRecordDO.setRemark(buyerBalanceRecord.getRemark());
        return buyerBalanceRecordDO;
    }
}
