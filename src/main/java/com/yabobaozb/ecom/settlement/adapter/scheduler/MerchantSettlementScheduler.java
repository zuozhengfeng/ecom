package com.yabobaozb.ecom.settlement.adapter.scheduler;

import com.yabobaozb.ecom.merchant.adapter.local.MerchantLocalAdapter;
import com.yabobaozb.ecom.settlement.domain.command.SingleMerchantDailySettingCommand;
import com.yabobaozb.ecom.settlement.domain.service.MerchantSettlementDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class MerchantSettlementScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MerchantLocalAdapter merchantLocalAdapter;

    private final MerchantSettlementDomainService merchantSettlementDomainService;

    @Autowired
    public MerchantSettlementScheduler(MerchantLocalAdapter merchantLocalAdapter, MerchantSettlementDomainService merchantSettlementDomainService) {
        this.merchantLocalAdapter = merchantLocalAdapter;
        this.merchantSettlementDomainService = merchantSettlementDomainService;
    }

    /** 每日结算 */
    @Scheduled(cron = "0 0 2 * * ?")
    public void runDailySettlement() {

        // 初始化结算日期
        // 日期使用昨日，格式yyyyMMdd，时间范围是昨日凌晨到今日凌晨
        LocalDateTime beginAt = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime endAt = LocalDate.now().atStartOfDay();
        String settleTime = beginAt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 简化，不作分页
        List<Long> merchantIds = merchantLocalAdapter.listAllValidMerchantIds();
        final int totalCount = merchantIds.size();
        int currentCount = 0;

        // 简化，串性执行
        for (Long merchantId : merchantIds) {
            currentCount++;

            try {
                SingleMerchantDailySettingCommand command = new SingleMerchantDailySettingCommand(merchantId, settleTime, beginAt, endAt, merchantSettlementDomainService);
                command.execute();
            } catch (Exception e) {
                logger.error("商户结算失败：{}", e.getMessage());

                // TODO 可以增加其他记录
            } finally {
                logger.info( String.format("执行进度， %d / %d", currentCount, totalCount) );
            }
        }

    }


}
