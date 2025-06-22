package com.yabobaozb.ecom.settlement.adapter.response;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.infra.enums.SettlementResult;
import lombok.Getter;

import java.math.BigDecimal;


public class MerchantDailySettlementResponse {
    @Getter
    private final long settleId;
    @Getter
    private final long merchantId;
    @Getter
    private final String settleTime;
    @Getter
    private final BigDecimal expectAmount;
    @Getter
    private final BigDecimal settleAmount;
    @Getter
    private final BigDecimal diffAmount;
    @Getter
    private final String remark;
    @Getter
    private final String settleResult;

    public MerchantDailySettlementResponse(long settleId, long merchantId, String settleTime, BigDecimal expectAmount, BigDecimal settleAmount, BigDecimal diffAmount, String remark, SettlementResult settleResult) {
        this.settleId = settleId;
        this.merchantId = merchantId;
        this.settleTime = settleTime;
        this.expectAmount = expectAmount;
        this.settleAmount = settleAmount;
        this.diffAmount = diffAmount;
        this.remark = remark;
        this.settleResult = settleResult.getDesc();
    }

    public static class Converter {
        public static MerchantDailySettlementResponse convert(MerchantDailySettlement settlement) {
            return new MerchantDailySettlementResponse(settlement.getSettleId(), settlement.getMerchantId(), settlement.getSettleTime(), settlement.getExpectAmount(), settlement.getSettleAmount(), settlement.getDiffAmount(), settlement.getRemark(), settlement.getSettleResult());
        }
    }



}
