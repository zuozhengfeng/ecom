package com.yabobaozb.ecom.buyer.domain;

import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

public class BuyerBalance {

    @Getter
    private final long buyerId;

    @Getter
    private BigDecimal balance;

    @Getter
    private long payId;

    @Getter
    private long newVersion;

    @Getter
    private final long version;

    @Getter
    private BuyerBalanceRecord record;

    public BuyerBalance(long buyerId) {
        this(buyerId, BigDecimal.ZERO);
    }

    public BuyerBalance(long buyerId, BigDecimal balance) {
        this(buyerId, balance, -1);
    }

    public BuyerBalance(long buyerId, BigDecimal balance, long version) {
        this.buyerId = buyerId;
        this.balance = balance;
        this.version = version;
    }

    public void refreshVersion() {
        this.newVersion = this.version + 1;
    }

    public void refreshPayId(long payId) {
        Objects.requireNonNull(this.record);
        this.payId = payId;

        this.record.refreshPayId(payId);
    }
    public void increase(BigDecimal amount, BuyerBalanceChangeType buyerBalanceChangeType, String remark) {
        if ( amount.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new RuntimeException("充值金额必须大于0");
        }
        BigDecimal balanceFrom = this.balance;
        this.balance = this.balance.add(amount);

        this.record = new BuyerBalanceRecord(this.buyerId, amount, balanceFrom, balance, buyerBalanceChangeType, remark );
    }

    public void decrease(BigDecimal amount, BuyerBalanceChangeType buyerBalanceChangeType, String remark) {
        if ( amount.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new RuntimeException("消费金额必须大于0");
        }

        BigDecimal balanceFrom = this.balance;
        this.balance = this.balance.subtract(amount);

        if ( this.balance.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new RuntimeException("余额不足");
        }

        this.record = new BuyerBalanceRecord(this.buyerId, amount, balanceFrom, balance, buyerBalanceChangeType, remark );
    }
}
