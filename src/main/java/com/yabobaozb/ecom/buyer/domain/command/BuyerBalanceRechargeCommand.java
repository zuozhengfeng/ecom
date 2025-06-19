package com.yabobaozb.ecom.buyer.domain.command;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.domain.service.BuyerBalanceDomainService;
import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import com.yabobaozb.ecom.common.ICommand;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuyerBalanceRechargeCommand implements ICommand<BuyerBalance> {

    private final long buyerId;
    private final BigDecimal amount;
    private final BuyerBalanceChangeType buyerBalanceChangeType;
    private final String remark;
    private final BuyerBalanceDomainService buyerBalanceDomainService;

    public BuyerBalanceRechargeCommand(long buyerId, String amount, BuyerBalanceChangeType buyerBalanceChangeType, String remark, BuyerBalanceDomainService buyerBalanceDomainService) {
        this.buyerId = buyerId;
        this.amount = new BigDecimal(amount);
        this.buyerBalanceChangeType = buyerBalanceChangeType;
        this.remark = remark;
        this.buyerBalanceDomainService = buyerBalanceDomainService;
    }

    public BuyerBalance execute() {
        return buyerBalanceDomainService.recharge(buyerId, amount, buyerBalanceChangeType, remark);
    }

}
