package com.yabobaozb.ecom.merchant.infra.model.converter;

import com.yabobaozb.ecom.merchant.domain.MerchantBalance;
import com.yabobaozb.ecom.merchant.infra.model.MerchantBalanceDO;
import com.yabobaozb.ecom.merchant.infra.model.MerchantBalanceVersionDO;

public final class MerchantBalanceConverter {

    public static MerchantBalance convertToAggregate(MerchantBalanceDO merchantBalanceDO) {
        return new MerchantBalance(merchantBalanceDO.getMerchantId(), merchantBalanceDO.getBalance(), merchantBalanceDO.getVersion());
    }

    public static MerchantBalanceDO convertToDO(MerchantBalance merchantBalance) {
        MerchantBalanceDO merchantBalanceDO = new MerchantBalanceDO();
        merchantBalanceDO.setMerchantId(merchantBalance.getMerchantId());
        merchantBalanceDO.setBalance(merchantBalance.getBalance());
        merchantBalanceDO.setVersion(merchantBalance.getNewVersion());
        return merchantBalanceDO;
    }

    public static MerchantBalanceVersionDO convertToVersionDO(MerchantBalance merchantBalance) {
        MerchantBalanceVersionDO merchantBalanceDO = new MerchantBalanceVersionDO();
        merchantBalanceDO.setMerchantId(merchantBalance.getMerchantId());
        merchantBalanceDO.setBalance(merchantBalance.getBalance());
        merchantBalanceDO.setVersion(merchantBalance.getNewVersion());
        merchantBalanceDO.setOldVersion(merchantBalance.getVersion());
        return merchantBalanceDO;
    }
}
