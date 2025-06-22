package com.yabobaozb.ecom.settlement.infra.model.converter;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlementRecord;
import com.yabobaozb.ecom.settlement.infra.enums.SettlementResult;
import com.yabobaozb.ecom.settlement.infra.model.SettlementInfoRecordDO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class SettlementInfoRecordConverter {

    public static SettlementInfoRecordDO convertToDO(MerchantDailySettlementRecord settlementRecord) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String begAt = settlementRecord.getBeginAt().format(formatter);
        String endAt = settlementRecord.getEndAt().format(formatter);

        SettlementInfoRecordDO settlementInfoRecordDO = new SettlementInfoRecordDO();
        settlementInfoRecordDO.setRecordId(settlementRecord.getRecordId());
        settlementInfoRecordDO.setSettleId(settlementRecord.getSettleId());
        settlementInfoRecordDO.setMerchantId(settlementRecord.getMerchantId());
        settlementInfoRecordDO.setSettleTime(settlementRecord.getSettleTime());
        settlementInfoRecordDO.setBeginAt(begAt);
        settlementInfoRecordDO.setEndAt(endAt);
        settlementInfoRecordDO.setExpectAmount(settlementRecord.getExpectAmount());
        settlementInfoRecordDO.setSettleAmount(settlementRecord.getSettleAmount());
        settlementInfoRecordDO.setDiffAmount(settlementRecord.getDiffAmount());
        settlementInfoRecordDO.setSettleResult((short)settlementRecord.getSettleResult().getValue());
        settlementInfoRecordDO.setRemark(settlementRecord.getRemark());
        settlementInfoRecordDO.setVersion(settlementRecord.getVersion());
        return settlementInfoRecordDO;
    }

    public static MerchantDailySettlementRecord convertToAggregate(SettlementInfoRecordDO settlementInfoRecordDO) {
        // 将yyyy-MM-dd hh:mm:ss的字符串转成LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime beginAt = LocalDateTime.parse(settlementInfoRecordDO.getBeginAt(), formatter);
        LocalDateTime endAt = LocalDateTime.parse(settlementInfoRecordDO.getEndAt(), formatter);
        return new MerchantDailySettlementRecord(
            settlementInfoRecordDO.getRecordId(), settlementInfoRecordDO.getSettleId(),  settlementInfoRecordDO.getMerchantId(),
            settlementInfoRecordDO.getSettleTime(), beginAt, endAt,
            settlementInfoRecordDO.getExpectAmount(), settlementInfoRecordDO.getSettleAmount(), settlementInfoRecordDO.getDiffAmount(),
            SettlementResult.parseByValue(settlementInfoRecordDO.getSettleResult()),
            settlementInfoRecordDO.getRemark(), settlementInfoRecordDO.getVersion());
    }
}
