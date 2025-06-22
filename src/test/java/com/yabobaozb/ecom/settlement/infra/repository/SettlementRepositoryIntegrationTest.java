package com.yabobaozb.ecom.settlement.infra.repository;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlementRecord;
import com.yabobaozb.ecom.settlement.infra.repository.impl.SettlementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SettlementRepositoryIntegrationTest {

    @Autowired
    private SettlementRepository settlementRepository;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testSaveSettlement_Normal() {
        LocalDateTime endAt = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime beginAt = endAt.minusDays(1);
        String settleTime = beginAt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        long merchantId = 20000L;


        MerchantDailySettlement settlement =
            MerchantDailySettlement.buildNormalRecordReport(
                merchantId, settleTime, beginAt, endAt, BigDecimal.valueOf(100), BigDecimal.valueOf(100));

        // 执行保存操作
        settlementRepository.saveSettlement(settlement);

        // 验证数据是否成功保存
        MerchantDailySettlement savedSettlement = settlementRepository.getByMerchantAndSettleTime(merchantId, settleTime);
        assertNotNull(savedSettlement);
        assertEquals(settlement.getMerchantId(), savedSettlement.getMerchantId());
        assertEquals(settlement.getSettleTime(), savedSettlement.getSettleTime());
        assertEquals(settlement.getNewVersion(), savedSettlement.getVersion());
        assertEquals(0, settlement.getExpectAmount().compareTo(savedSettlement.getExpectAmount()));
        assertEquals(0, settlement.getSettleAmount().compareTo(savedSettlement.getSettleAmount()));

        MerchantDailySettlementRecord record = settlementRepository.getRecentRecordByMerchantAndSettleTime(merchantId, settleTime);
        assertNotNull(record);
        assertEquals(0, settlement.getExpectAmount().compareTo(record.getExpectAmount()));
        assertEquals(0, settlement.getSettleAmount().compareTo(record.getSettleAmount()));
        assertEquals(0, settlement.getDiffAmount().compareTo(record.getDiffAmount()));
        assertEquals(settlement.getSettleResult(), record.getSettleResult());
        assertEquals(settlement.getRemark(), record.getRemark());
        assertEquals(settlement.getNewVersion(), record.getVersion());
        assertEquals(settlement.getSettleId(), record.getSettleId());

    }
}