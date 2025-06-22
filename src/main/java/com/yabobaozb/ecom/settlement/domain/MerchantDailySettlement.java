package com.yabobaozb.ecom.settlement.domain;

import com.yabobaozb.ecom.common.infra.util.IDGenerator;
import com.yabobaozb.ecom.settlement.infra.enums.SettlementResult;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MerchantDailySettlement {

    private final long settleId;

    private final long merchantId;

    private final String settleTime;

    private final LocalDateTime beginAt;

    private final LocalDateTime endAt;

    private final BigDecimal expectAmount;

    private final BigDecimal settleAmount;

    private final BigDecimal diffAmount;

    private final SettlementResult settleResult;

    private String remark;

    private final long version;

    private final long newVersion;

    private MerchantDailySettlementRecord settlementRecord;

    private MerchantDailySettlement(long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt, long version) {
        this(merchantId, settleTime, beginAt, endAt, BigDecimal.ZERO, BigDecimal.ZERO, version);

    }

    private MerchantDailySettlement(long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt, BigDecimal expectAmount, BigDecimal settleAmount, long version) {
        this.merchantId = merchantId;
        this.settleTime = settleTime;
        this.beginAt = beginAt;
        this.endAt = endAt;
        this.expectAmount = expectAmount;
        this.settleAmount = settleAmount;
        this.diffAmount = settleAmount.subtract(expectAmount);

        this.settleResult = ( 0 == expectAmount.compareTo(settleAmount) ) ? SettlementResult.MATCHED : SettlementResult.NOTMATCHED;
        this.remark = this.settleResult ==SettlementResult.MATCHED ? "结算正常" : String.format("预计结算%s元，实际结算%s元, 差异%s元", expectAmount, settleAmount, diffAmount);

        this.settleId = generateSettleId();
        this.version = version;
        this.newVersion = version + 1;
    }

    public MerchantDailySettlement(long settleId, long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt, BigDecimal expectAmount, BigDecimal settleAmount, BigDecimal diffAmount, String remark, SettlementResult settleResult, long version) {
        this.merchantId = merchantId;
        this.settleTime = settleTime;
        this.beginAt = beginAt;
        this.endAt = endAt;
        this.expectAmount = expectAmount;
        this.settleAmount = settleAmount;
        this.diffAmount = diffAmount;

        this.settleResult = settleResult;
        this.remark = remark;
        this.settleId = settleId;
        this.version = version;
        this.newVersion = version + 1;
    }

    private long generateSettleId() {
        return IDGenerator.generateId();
    }

    private void initSettlementRecord() {
        this.settlementRecord = new MerchantDailySettlementRecord(this);
    }

    public static MerchantDailySettlement buildEmptyRecordReport(long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt) {
        return buildEmptyRecordReport(merchantId, settleTime, beginAt, endAt, -1);
    }
    public static MerchantDailySettlement buildEmptyRecordReport(long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt, long version) {
        MerchantDailySettlement merchantDailySettlement = new MerchantDailySettlement(merchantId, settleTime, beginAt, endAt, version);
        merchantDailySettlement.remark = "结算正常；商家无订单数据";
        merchantDailySettlement.initSettlementRecord();
        return merchantDailySettlement;
    }

    public static MerchantDailySettlement buildNormalRecordReport(long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt, BigDecimal orderAmount, BigDecimal settleAmount) {
        return buildNormalRecordReport(merchantId, settleTime, beginAt, endAt, orderAmount, settleAmount, -1L);
    }
    public static MerchantDailySettlement buildNormalRecordReport(long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt, BigDecimal orderAmount, BigDecimal settleAmount, long version) {
        MerchantDailySettlement merchantDailySettlement = new MerchantDailySettlement(merchantId, settleTime, beginAt, endAt, orderAmount, settleAmount, version);
        merchantDailySettlement.initSettlementRecord();
        return merchantDailySettlement;
    }
}
