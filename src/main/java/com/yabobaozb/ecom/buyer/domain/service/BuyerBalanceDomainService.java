package com.yabobaozb.ecom.buyer.domain.service;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import com.yabobaozb.ecom.buyer.infra.repository.IBuyerBalanceRepository;
import com.yabobaozb.ecom.payment.adapter.local.PaymentLocalAdapter;
import com.yabobaozb.ecom.payment.infra.enums.PayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BuyerBalanceDomainService {

    private final IBuyerBalanceRepository buyerBalanceRepository;

    private final PaymentLocalAdapter paymentLocalAdapter;

    @Autowired
    public BuyerBalanceDomainService(IBuyerBalanceRepository buyerBalanceRepository, PaymentLocalAdapter paymentLocalAdapter) {
        this.buyerBalanceRepository = buyerBalanceRepository;
        this.paymentLocalAdapter = paymentLocalAdapter;
    }


    public BuyerBalance getByBuyerId(long buyerId) {
        return buyerBalanceRepository.getByBuyerId(buyerId);
    }

    @Transactional
    public BuyerBalance recharge(long buyerId, BigDecimal amount, BuyerBalanceChangeType buyerBalanceChangeType, String remark) {
        BuyerBalance buyerBalance = buildBuyerBalance(buyerId);
        buyerBalance.increase(amount, buyerBalanceChangeType, remark);

        // 增加支付记录（现金支付）
        long payId = paymentLocalAdapter.addPayment(buyerId, amount, PayType.CASH, buyerBalance.getRecord().getRecordId(), remark);
        buyerBalance.refreshPayId(payId);

        // 更新余额及余额变动记录
        buyerBalance.refreshVersion();
        buyerBalanceRepository.refreshBalance(buyerBalance);

        return buyerBalance;
    }

    private BuyerBalance buildBuyerBalance(long buyerId) {
        BuyerBalance buyerBalance = getByBuyerId(buyerId);
        if ( null == buyerBalance ) {
            buyerBalance = new BuyerBalance(buyerId, BigDecimal.ZERO);
        }
        return buyerBalance;
    }

    @Transactional
    public BuyerBalance decreaseBalance(long buyerId, BigDecimal amount, long orderId, BuyerBalanceChangeType buyerBalanceChangeType, String remark) {
        BuyerBalance buyerBalance = buildBuyerBalance(buyerId);
        buyerBalance.decrease(amount, buyerBalanceChangeType, remark);

        // 增加支付记录（余额支付）
        long payId = paymentLocalAdapter.addPayment(buyerId, amount, PayType.BALANCE, orderId, remark);
        buyerBalance.refreshPayId(payId);

        // 更新余额
        buyerBalance.refreshVersion();
        buyerBalanceRepository.refreshBalance(buyerBalance);

        return buyerBalance;
    }
}
