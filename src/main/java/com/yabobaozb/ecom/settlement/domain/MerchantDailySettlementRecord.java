package com.yabobaozb.ecom.settlement.domain;

import com.yabobaozb.ecom.common.infra.util.IDGenerator;
import com.yabobaozb.ecom.settlement.infra.enums.SettlementResult;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MerchantDailySettlementRecord {

    private final long recordId;

    private final long settleId;

    private final long merchantId;

    private final String settleTime;

    private final LocalDateTime beginAt;

    private final LocalDateTime endAt;

    private final BigDecimal expectAmount;

    private final BigDecimal settleAmount;

    private final BigDecimal diffAmount;

    private final SettlementResult settleResult;

    private final String remark;

    private final long version;

    public MerchantDailySettlementRecord(MerchantDailySettlement settlement) {
        this.settleId = settlement.getSettleId();
        this.merchantId = settlement.getMerchantId();
        this.settleTime = settlement.getSettleTime();
        this.beginAt = settlement.getBeginAt();
        this.endAt = settlement.getEndAt();
        this.expectAmount = settlement.getExpectAmount();
        this.settleAmount = settlement.getSettleAmount();
        this.diffAmount = settlement.getDiffAmount();
        this.settleResult = settlement.getSettleResult();
        this.remark = settlement.getRemark();
        this.version = settlement.getNewVersion();

        this.recordId = generateRecordId();
    }

    public MerchantDailySettlementRecord(long recordId, long settleId, long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt, BigDecimal expectAmount, BigDecimal settleAmount, BigDecimal diffAmount, SettlementResult settleResult, String remark, long version) {
        this.recordId = recordId;
        this.settleId = settleId;
        this.merchantId = merchantId;
        this.settleTime = settleTime;
        this.beginAt = beginAt;
        this.endAt = endAt;
        this.expectAmount = expectAmount;
        this.settleAmount = settleAmount;
        this.diffAmount = diffAmount;
        this.settleResult = settleResult;
        this.remark = remark;
        this.version = version;
    }

    private long generateRecordId() {
        return IDGenerator.generateId();
    }


}
