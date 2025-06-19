package com.yabobaozb.ecom.merchant.domain;

import com.yabobaozb.ecom.merchant.domain.enums.MerchantBalanceChangeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MerchantBalanceUnitTest {

    private MerchantBalance merchantBalance;

    @BeforeEach
    void setUp() {
        merchantBalance = new MerchantBalance(1L, BigDecimal.valueOf(100.00));
    }

    @Test
    void testIncrease_normal() {
        BigDecimal initialBalance = merchantBalance.getBalance();
        BigDecimal increaseAmount = new BigDecimal("50.00");
        MerchantBalanceChangeType changeType = MerchantBalanceChangeType.fromSell;

        String remark = "test remark";
        long payId = 1L;
        long sourceOrderId = 1L;

        merchantBalance.increase(increaseAmount, changeType, payId, sourceOrderId, remark);
        assertEquals(initialBalance.add(increaseAmount), merchantBalance.getBalance());

        MerchantBalanceRecord record = merchantBalance.getRecord();
        assertNotNull(record);
        assertTrue(record.getRecordId() > 0);
        assertEquals(merchantBalance.getMerchantId(), record.getMerchantId());
        assertEquals(increaseAmount, record.getAmount());
        assertEquals(initialBalance, record.getBalanceFrom());
        assertEquals(merchantBalance.getBalance(), record.getBalanceTo());
        assertEquals(changeType, record.getChangeType());
        assertEquals(payId, record.getPayId());
        assertEquals(sourceOrderId, record.getSourceOrderId());
        assertEquals(remark, record.getRemark());
    }

    @Test
    void testIncrease_withZeroAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        MerchantBalanceChangeType changeType = MerchantBalanceChangeType.fromSell;
        String remark = "test remark";

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                merchantBalance.increase(amount, changeType, 1L, 1L, remark));
        assertEquals("金额必须大于0", exception.getMessage());
    }

    @Test
    void testIncrease_withNegativeAmount() {
        BigDecimal amount = new BigDecimal("-100.00");
        MerchantBalanceChangeType changeType = MerchantBalanceChangeType.fromSell;
        String remark = "test remark";

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                merchantBalance.increase(amount, changeType, 1L, 1L, remark));
        assertEquals("金额必须大于0", exception.getMessage());
    }


}
