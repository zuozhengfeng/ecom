package com.yabobaozb.ecom.settlement.adapter.rest;

import com.yabobaozb.ecom.common.ReturnResult;
import com.yabobaozb.ecom.settlement.adapter.response.MerchantDailySettlementResponse;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.domain.command.SingleMerchantDailySettingCommand;
import com.yabobaozb.ecom.settlement.domain.service.MerchantSettlementDomainService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/settlement")
public class MerchantSettlementRestAdapter {

    private final MerchantSettlementDomainService merchantSettlementDomainService;
    public MerchantSettlementRestAdapter(MerchantSettlementDomainService merchantSettlementDomainService) {
        this.merchantSettlementDomainService = merchantSettlementDomainService;
    }

    // 按日结算Rest接口方便随时结算，内部有版本快照记录
    @PostMapping("/daily")
    public ReturnResult<MerchantDailySettlementResponse> dailySettlement(@RequestParam("merchantId") long merchantId,
                                                                         @RequestParam("settleTime") String settleTime) {
        LocalDateTime[] settleTimes = parseSettleTime(settleTime);
        LocalDateTime beginAt = settleTimes[0];
        LocalDateTime endAt = settleTimes[1];

        SingleMerchantDailySettingCommand command = new SingleMerchantDailySettingCommand(merchantId, settleTime, beginAt, endAt, merchantSettlementDomainService);
        MerchantDailySettlement merchantDailySettlement = command.execute();

        ReturnResult<MerchantDailySettlementResponse> result = new ReturnResult<>();
        result.setData(MerchantDailySettlementResponse.Converter.convert(merchantDailySettlement));
        return result;
    }

    // 获取开始和结束时间
    // settleTime格式是yyyyMMdd
    private LocalDateTime[] parseSettleTime(String settleTime) {
        LocalDateTime beginAt = LocalDate.parse(settleTime, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
        LocalDateTime endAt = beginAt.plusDays(1);
        return new LocalDateTime[]{beginAt, endAt};
    }

}
