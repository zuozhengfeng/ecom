package com.yabobaozb.ecom.merchant.domain.service;

import com.yabobaozb.ecom.merchant.domain.MerchantBalance;
import com.yabobaozb.ecom.merchant.domain.enums.MerchantBalanceChangeType;
import com.yabobaozb.ecom.merchant.infra.repository.IMerchantBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MerchantBalanceDomainService {

    private final IMerchantBalanceRepository merchantBalanceRepository;

    @Autowired
    public MerchantBalanceDomainService(IMerchantBalanceRepository merchantBalanceRepository) {
        this.merchantBalanceRepository = merchantBalanceRepository;
    }

    @Transactional
    public MerchantBalance increaseBalance(long merchantId, BigDecimal amount, long payId, long sourceOrderId, String remark) {
        MerchantBalance merchantBalance = buildMerchantBalance(merchantId);
        merchantBalance.increase(amount, MerchantBalanceChangeType.fromSell, payId, sourceOrderId, remark);

        // 更新余额及余额变动记录
        merchantBalance.refreshVersion();
        merchantBalanceRepository.refreshBalance(merchantBalance);

        return merchantBalance;
    }

    public MerchantBalance decreaseBalance(long merchantId, BigDecimal amount, String remark) {
        // 暂时不支持，没有具体的使用场景
        throw new UnsupportedOperationException();
    }

    private MerchantBalance buildMerchantBalance(long merchantId) {
        MerchantBalance merchantBalance = getMerchantBalance(merchantId);
        if ( null == merchantBalance ) {
            merchantBalance = new MerchantBalance(merchantId, BigDecimal.ZERO);
        }
        return merchantBalance;
    }

    public MerchantBalance getMerchantBalance(long merchantId) {
        return merchantBalanceRepository.getByMerchantId(merchantId);
    }
}
