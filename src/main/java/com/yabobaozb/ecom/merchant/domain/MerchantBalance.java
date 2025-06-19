package com.yabobaozb.ecom.merchant.domain;

import com.yabobaozb.ecom.merchant.domain.enums.MerchantBalanceChangeType;
import lombok.Getter;

import java.math.BigDecimal;

public class MerchantBalance {

    @Getter
    private final long merchantId;
    @Getter
    private BigDecimal balance;
    @Getter
    private long version;
    @Getter
    private long newVersion;

    @Getter
    private MerchantBalanceRecord record;

    public MerchantBalance(long merchantId) {
        this(merchantId, BigDecimal.ZERO, -1);
    }

    public MerchantBalance(long merchantId, BigDecimal balance) {
        this(merchantId, balance, -1);
    }

    public MerchantBalance(long merchantId, BigDecimal balance, long version) {
        this.merchantId = merchantId;
        this.balance = balance;
        this.version = version;
    }

    public void increase(BigDecimal amount, MerchantBalanceChangeType changeType, long payId, long sourceOrderId, String remark) {
        if ( amount.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new RuntimeException("金额必须大于0");
        }
        BigDecimal balanceFrom = this.balance;
        this.balance = this.balance.add(amount);

        this.record = new MerchantBalanceRecord( this.merchantId, amount, balanceFrom, this.balance, changeType, payId, sourceOrderId, remark );
    }

    public void decrease(BigDecimal amount, MerchantBalanceChangeType changeType, String remark) {
        if ( amount.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new RuntimeException("金额必须大于0");
        }

        BigDecimal balanceFrom = this.balance;
        this.balance = this.balance.subtract(amount);

        if ( this.balance.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new RuntimeException("余额不足");
        }

        this.record = new MerchantBalanceRecord( this.merchantId, amount, balanceFrom, this.balance, changeType, 0L, 0L, remark );
    }

    public void refreshVersion() {
        this.newVersion = this.version + 1;
    }
}
