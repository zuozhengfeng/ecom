package com.yabobaozb.ecom.merchant.domain;

import com.yabobaozb.ecom.common.infra.util.IDGenerator;
import com.yabobaozb.ecom.merchant.domain.enums.MerchantBalanceChangeType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MerchantBalanceRecord {

    private final long recordId;

    private final long merchantId;

    private final BigDecimal amount;

    private final BigDecimal balanceFrom;

    private final BigDecimal balanceTo;

    private final MerchantBalanceChangeType changeType;

    private final long payId;

    private final long sourceOrderId;

    private final String remark;

    private LocalDateTime createTime;

    public MerchantBalanceRecord(long recordId, long merchantId, BigDecimal amount, BigDecimal balanceFrom, BigDecimal balanceTo, int changeType, long payId, long sourceOrderId, String remark, LocalDateTime createTime) {
        this.recordId = recordId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.balanceFrom = balanceFrom;
        this.balanceTo = balanceTo;
        this.changeType = MerchantBalanceChangeType.getByValue(changeType);
        this.payId = payId;
        this.sourceOrderId = sourceOrderId;
        this.remark = remark;
        this.createTime = createTime;
    }

    public MerchantBalanceRecord(long merchantId, BigDecimal amount, BigDecimal balanceFrom, BigDecimal balanceTo, MerchantBalanceChangeType changeType, long payId, long sourceOrderId, String remark) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.balanceFrom = balanceFrom;
        this.balanceTo = balanceTo;
        this.changeType = changeType;
        this.payId = payId;
        this.sourceOrderId = sourceOrderId;
        this.remark = remark;

        this.recordId = generateRecordId();
    }


    private long generateRecordId() {
        return IDGenerator.generateId();
    }

}
