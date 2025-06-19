package com.yabobaozb.ecom.buyer.domain;

import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BuyerBalanceUnitTest {

    private BuyerBalance buyerBalance;

    private final long buyerId = 1001L;

    @BeforeEach
    void setUp() {
        // 初始化买家ID为1001，初始余额为500
        buyerBalance = new BuyerBalance(buyerId, new BigDecimal("500"));
    }

    /**
     * 充值金额为正数，验证余额更新和记录生成
     */
    @Test
    void testIncrease_withValidAmount() {
        BigDecimal amount = new BigDecimal("100");
        buyerBalance.increase(amount, BuyerBalanceChangeType.Recharge, "充值");

        // 验证余额更新正确
        assertEquals(new BigDecimal("600"), buyerBalance.getBalance());

        // 验证记录内容正确
        BuyerBalanceRecord record = buyerBalance.getRecord();
        assertNotNull(record);
        assertEquals(buyerId, record.getBuyerId());
        assertEquals(amount, record.getAmount());
        assertEquals(new BigDecimal("500"), record.getBalanceFrom());
        assertEquals(new BigDecimal("600"), record.getBalanceTo());
        assertEquals(BuyerBalanceChangeType.Recharge, record.getBuyerBalanceChangeType());
        assertEquals("充值", record.getRemark());
    }

    /**
     * 充值金额为小数，备注为null的情况
     */
    @Test
    void testIncrease_withDecimalAmountAndNullRemark() {
        BigDecimal amount = new BigDecimal("50.5");
        buyerBalance.increase(amount, BuyerBalanceChangeType.Purchase, null);

        // 验证余额更新正确
        assertEquals(new BigDecimal("550.5"), buyerBalance.getBalance());

        // 验证记录内容正确
        BuyerBalanceRecord record = buyerBalance.getRecord();
        assertNotNull(record);
        assertNull(record.getRemark());
    }

    /**
     * 充值金额为负数，抛出异常
     */
    @Test
    void testIncrease_withNegativeAmount() {
        BigDecimal amount = new BigDecimal("-100");
        assertThrows(RuntimeException.class, () -> {
            buyerBalance.increase(amount, BuyerBalanceChangeType.Recharge, "充值");
        });
    }

    /**
     * 充值金额为0，抛出异常
     */
    @Test
    void testIncrease_withZeroAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        assertThrows(RuntimeException.class, () -> {
            buyerBalance.increase(amount, BuyerBalanceChangeType.Recharge, "充值");
        });
    }

}
