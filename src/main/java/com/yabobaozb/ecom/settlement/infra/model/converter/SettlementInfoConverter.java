package com.yabobaozb.ecom.settlement.infra.model.converter;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.infra.enums.SettlementResult;
import com.yabobaozb.ecom.settlement.infra.model.SettlementInfoDO;
import com.yabobaozb.ecom.settlement.infra.model.SettlementInfoVersionDO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class SettlementInfoConverter {

    public static SettlementInfoDO convertToDO(MerchantDailySettlement merchantDailySettlement) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String begAt = merchantDailySettlement.getBeginAt().format(formatter);
        String endAt = merchantDailySettlement.getEndAt().format(formatter);

        SettlementInfoVersionDO settlementInfoDO = new SettlementInfoVersionDO();
        settlementInfoDO.setSettleId(merchantDailySettlement.getSettleId());
        settlementInfoDO.setMerchantId(merchantDailySettlement.getMerchantId());
        settlementInfoDO.setSettleTime(merchantDailySettlement.getSettleTime());
        settlementInfoDO.setBeginAt(begAt);
        settlementInfoDO.setEndAt(endAt);
        settlementInfoDO.setExpectAmount(merchantDailySettlement.getExpectAmount());
        settlementInfoDO.setSettleAmount(merchantDailySettlement.getSettleAmount());
        settlementInfoDO.setDiffAmount(merchantDailySettlement.getDiffAmount());
        settlementInfoDO.setSettleResult((short) merchantDailySettlement.getSettleResult().getValue());
        settlementInfoDO.setRemark(merchantDailySettlement.getRemark());

        settlementInfoDO.setVersion(merchantDailySettlement.getNewVersion());
        return settlementInfoDO;
    }

    public static SettlementInfoVersionDO convertToVersionDO(MerchantDailySettlement merchantDailySettlement) {
        SettlementInfoVersionDO versionDO = (SettlementInfoVersionDO)convertToDO(merchantDailySettlement);
        versionDO.setOldVersion(merchantDailySettlement.getVersion());
        return versionDO;
    }

    public static MerchantDailySettlement convertToAggregate(SettlementInfoDO settlementInfoDO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime begAt = LocalDateTime.parse(settlementInfoDO.getBeginAt(), formatter);
        LocalDateTime endAt = LocalDateTime.parse(settlementInfoDO.getEndAt(), formatter);

        return new MerchantDailySettlement(
                settlementInfoDO.getSettleId(),
                settlementInfoDO.getMerchantId(),
                settlementInfoDO.getSettleTime(),
                begAt,
                endAt,
                settlementInfoDO.getExpectAmount(),
                settlementInfoDO.getSettleAmount(),
                settlementInfoDO.getDiffAmount(),
                settlementInfoDO.getRemark(),
                SettlementResult.parseByValue(settlementInfoDO.getSettleResult()),
                settlementInfoDO.getVersion()
        );
    }
}
