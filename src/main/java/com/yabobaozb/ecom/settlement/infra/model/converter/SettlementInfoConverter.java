package com.yabobaozb.ecom.settlement.infra.model.converter;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.infra.model.SettlementInfoDO;

import java.time.format.DateTimeFormatter;

public final class SettlementInfoConverter {

    public static SettlementInfoDO convertToDO(MerchantDailySettlement merchantDailySettlement) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String begAt = merchantDailySettlement.getBeginAt().format(formatter);
        String endAt = merchantDailySettlement.getEndAt().format(formatter);

        SettlementInfoDO settlementInfoDO = new SettlementInfoDO();
        settlementInfoDO.setMerchantId(merchantDailySettlement.getMerchantId());
        settlementInfoDO.setSettleTime(merchantDailySettlement.getSettleTime());
        settlementInfoDO.setBeginAt(begAt);
        settlementInfoDO.setEndAt(endAt);
        settlementInfoDO.setExpectAmount(merchantDailySettlement.getExpectAmount());
        settlementInfoDO.setSettleAmount(merchantDailySettlement.getSettleAmount());
        settlementInfoDO.setDiffAmount(merchantDailySettlement.getDiffAmount());
        settlementInfoDO.setSettleResult((short) merchantDailySettlement.getSettleResult().getValue());
        settlementInfoDO.setRemark(merchantDailySettlement.getRemark());
        return settlementInfoDO;
    }
}
