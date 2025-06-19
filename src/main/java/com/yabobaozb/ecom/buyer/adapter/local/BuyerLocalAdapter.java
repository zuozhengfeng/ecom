package com.yabobaozb.ecom.buyer.adapter.local;

import com.yabobaozb.ecom.buyer.adapter.response.SimpleBuyerBalanceResponse;
import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.domain.command.BuyerBalancePurchaseCommand;
import com.yabobaozb.ecom.buyer.domain.service.BuyerBalanceDomainService;
import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BuyerLocalAdapter {

    private final BuyerBalanceDomainService buyerBalanceDomainService;

    @Autowired
    public BuyerLocalAdapter(BuyerBalanceDomainService buyerBalanceDomainService) {
        this.buyerBalanceDomainService = buyerBalanceDomainService;
    }

    public SimpleBuyerBalanceResponse getByBuyerId(long buyerId) {
        BuyerBalance buyerBalance = buyerBalanceDomainService.getByBuyerId(buyerId);
        if ( null == buyerBalance ) {
            buyerBalance = new BuyerBalance(buyerId);
        }
        return SimpleBuyerBalanceResponse.Converter.convert(buyerBalance);
    }

    /** 扣减余额 */
    public SimpleBuyerBalanceResponse decreaseBalanceForPurchase(long buyerId, BigDecimal amount, long orderId) {
        BuyerBalancePurchaseCommand command = new BuyerBalancePurchaseCommand(buyerId, amount, orderId, BuyerBalanceChangeType.Purchase, "用户购买", buyerBalanceDomainService);
        return SimpleBuyerBalanceResponse.Converter.convert(command.execute());
    }
}
