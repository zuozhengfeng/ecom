package com.yabobaozb.ecom.buyer.infra.model.converter;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.infra.model.BuyerBalanceDO;
import com.yabobaozb.ecom.buyer.infra.model.BuyerBalanceVersionDO;

public final class BuyerBalanceConverter {

    public static BuyerBalanceDO convertToDO(BuyerBalance buyerBalance) {
        BuyerBalanceDO buyerBalanceDO = new BuyerBalanceDO();
        buyerBalanceDO.setBuyerId(buyerBalance.getBuyerId());
        buyerBalanceDO.setBalance(buyerBalance.getBalance());
        buyerBalanceDO.setVersion(buyerBalance.getNewVersion());
        return buyerBalanceDO;
    }

    public static BuyerBalance convertToAggregate(BuyerBalanceDO buyerBalanceDO) {
        return new BuyerBalance(buyerBalanceDO.getBuyerId(), buyerBalanceDO.getBalance(), buyerBalanceDO.getVersion());
    }

    public static BuyerBalanceVersionDO convertToVersionDO(BuyerBalance buyerBalance) {
        BuyerBalanceVersionDO buyerBalanceVersionDO = new BuyerBalanceVersionDO();
        buyerBalanceVersionDO.setBuyerId(buyerBalance.getBuyerId());
        buyerBalanceVersionDO.setBalance(buyerBalance.getBalance());
        buyerBalanceVersionDO.setVersion(buyerBalance.getNewVersion());
        buyerBalanceVersionDO.setOldVersion(buyerBalance.getVersion());
        return buyerBalanceVersionDO;
    }
}
