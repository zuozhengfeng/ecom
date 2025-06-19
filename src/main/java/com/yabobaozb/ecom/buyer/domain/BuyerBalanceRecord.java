package com.yabobaozb.ecom.buyer.domain;

import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import com.yabobaozb.ecom.common.infra.util.IDGenerator;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BuyerBalanceRecord {

    private long recordId;

    private long buyerId;

    private BigDecimal amount;

    private BigDecimal balanceFrom;

    private BigDecimal balanceTo;

    private BuyerBalanceChangeType buyerBalanceChangeType;

    private String remark;

    private long payId;

    public BuyerBalanceRecord(long buyerId, BigDecimal amount, BigDecimal balanceFrom, BigDecimal balanceTo, BuyerBalanceChangeType buyerBalanceChangeType, String remark) {
        this.buyerId = buyerId;
        this.amount = amount;
        this.balanceFrom = balanceFrom;
        this.balanceTo = balanceTo;
        this.buyerBalanceChangeType = buyerBalanceChangeType;
        this.remark = remark;

        this.recordId = generateRecordId();
    }

    private long generateRecordId() {
        return IDGenerator.generateId();
    }

    public void refreshPayId(long payId) {
        this.payId = payId;
    }
}
