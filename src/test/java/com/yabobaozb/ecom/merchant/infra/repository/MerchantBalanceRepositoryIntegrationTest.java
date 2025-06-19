package com.yabobaozb.ecom.merchant.infra.repository;

import com.yabobaozb.ecom.merchant.domain.MerchantBalance;
import com.yabobaozb.ecom.merchant.domain.MerchantBalanceRecord;
import com.yabobaozb.ecom.merchant.domain.enums.MerchantBalanceChangeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MerchantBalanceRepositoryIntegrationTest {

    private final IMerchantBalanceRepository merchantBalanceRepository;

    @Autowired
    public MerchantBalanceRepositoryIntegrationTest(IMerchantBalanceRepository merchantBalanceRepository) {
        this.merchantBalanceRepository = merchantBalanceRepository;
    }

    private final static long normalMerchantId = 20000L;
    private final static long nobalanceMerchantId = 20001L;
    private final static long notFoundMerchantId = 20002L;

    @BeforeEach
    void setUp() {
    }

    /**
     * 商户和余额记录都存在
     */
    @Test
    void testGetByMerchantId_normal() {
        long merchantId = normalMerchantId;
        MerchantBalance result = merchantBalanceRepository.getByMerchantId(merchantId);
        assertNotNull( result);
        assertEquals( result.getMerchantId(), merchantId );
    }

    /**
     * 商户存在，但余额记录不存在
     */
    @Test
    void testGetByMerchantId_BalanceNotExists_ShouldReturnNull() {
        long merchantId = nobalanceMerchantId;
        MerchantBalance result = merchantBalanceRepository.getByMerchantId(merchantId);
        assertNull(result);
    }

    /**
     * 商户不存在，应抛出异常
     */
    @Test
    void testGetByMerchantId_MerchantNotExists_ShouldThrowException() {
        long merchantId = notFoundMerchantId;

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> merchantBalanceRepository.getByMerchantId(merchantId));
        assertEquals("商家不存在", exception.getMessage());
    }

    /**
     * 更新余额, 版本号+1， 插入变动记录
     */
    @Test
    void testRefreshBalance_noraml_hasbalance() {
        long merchantId = normalMerchantId;
        BigDecimal increaseAmount = new BigDecimal("100.99");

        MerchantBalance merchantBalance = merchantBalanceRepository.getByMerchantId(merchantId);
        BigDecimal newBalance = merchantBalance.getBalance().add(increaseAmount);
        long newVersion = merchantBalance.getNewVersion()+1;
        merchantBalance.increase(increaseAmount, MerchantBalanceChangeType.fromSell, 1L, 1L, "充值");

        merchantBalance.refreshVersion();
        merchantBalanceRepository.refreshBalance(merchantBalance);

        merchantBalance = merchantBalanceRepository.getByMerchantId(merchantId);
        assertEquals(newBalance, merchantBalance.getBalance());
        assertEquals(newVersion, merchantBalance.getVersion());

        List<MerchantBalanceRecord> records = merchantBalanceRepository.getRecordsByMerchantId(merchantId);
        assertEquals(1, records.size());
    }


    /**
     * 插入新余额, 版本号为0， 插入变动记录
     */
    @Test
    void testRefreshBalance_noraml_nobalance() {
        long merchantId = nobalanceMerchantId;
        BigDecimal increaseAmount = new BigDecimal("100.99");

        MerchantBalance merchantBalance = new MerchantBalance(merchantId);
        BigDecimal newBalance = BigDecimal.ZERO.add(increaseAmount);
        long newVersion = 0;
        merchantBalance.increase(increaseAmount, MerchantBalanceChangeType.fromSell, 1L, 1L, "充值");

        merchantBalance.refreshVersion();
        merchantBalanceRepository.refreshBalance(merchantBalance);

        merchantBalance = merchantBalanceRepository.getByMerchantId(merchantId);
        assertEquals(newBalance, merchantBalance.getBalance());
        assertEquals(newVersion, merchantBalance.getVersion());

        List<MerchantBalanceRecord> records = merchantBalanceRepository.getRecordsByMerchantId(merchantId);
        assertEquals(1, records.size());
    }

    /**
     * 商户不存在时调用 refreshBalance 应抛出异常
     */
    @Test
    void testRefreshBalance_notFoundMerchantId() {
        long merchantId = notFoundMerchantId;

        // 任意初始化的商家
        MerchantBalance merchantBalance = new MerchantBalance(merchantId, BigDecimal.ONE);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> merchantBalanceRepository.refreshBalance(merchantBalance));
        assertEquals("商家不存在", exception.getMessage());
    }

}
