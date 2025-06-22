package com.yabobaozb.ecom.settlement.domain.command;

import com.yabobaozb.ecom.common.ICommand;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.domain.service.MerchantSettlementDomainService;

import java.time.LocalDateTime;

public class SingleMerchantDailySettingCommand implements ICommand<MerchantDailySettlement> {

//    private final SettlementPeriod settlePeriod = SettlementPeriod.DAILY;
    private final long merchantId;
    private final String settleDay;

    // 结算开始时间（含）
    private final LocalDateTime beginAt;


    // 结算开始时间（不含）
    private final LocalDateTime endAt;

    private final MerchantSettlementDomainService merchantSettlementDomainService;


    public SingleMerchantDailySettingCommand(long merchantId, String settleDay, LocalDateTime beginAt, LocalDateTime endAt, MerchantSettlementDomainService merchantSettlementDomainService) {
        this.merchantId = merchantId;
        this.settleDay = settleDay;
        this.beginAt = beginAt;
        this.endAt = endAt;
        this.merchantSettlementDomainService = merchantSettlementDomainService;
    }

    public MerchantDailySettlement execute() {
        return merchantSettlementDomainService.executeDailySettlement(merchantId, settleDay, beginAt, endAt);
    }
}
