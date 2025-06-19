package com.yabobaozb.ecom.buyer.domain.service;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BuyerBalanceDomainServiceIntegrationTest {


    @Autowired
    private BuyerBalanceDomainService buyerBalanceDomainService;

    private final long normalBuyerId = 10000L;
    private final long noBalanceBuyerId = 10001L;
    private final long notfoundBuyerId = 10002L;


    @Test
    void testRecharge_normalBuyer() {
        long buyerId = normalBuyerId;

        BigDecimal amount = new BigDecimal("100");
        BuyerBalanceChangeType buyerBalanceChangeType = BuyerBalanceChangeType.Recharge;
        String remark = "Test recharge";

        BuyerBalance buyerBalance = buyerBalanceDomainService.getByBuyerId(buyerId);
        assertNotNull(buyerBalance);
        assertNotNull(buyerBalance.getBalance());
        BigDecimal balance = buyerBalance.getBalance().add(amount);

        buyerBalance = buyerBalanceDomainService.recharge(buyerId, amount, buyerBalanceChangeType, remark);

        // 验证支付记录是否存在
        assertNotNull(buyerBalance.getRecord());
        assertEquals(buyerId, buyerBalance.getRecord().getBuyerId());
        assertEquals(0, amount.compareTo(buyerBalance.getRecord().getAmount()));
        assertEquals(buyerBalanceChangeType, buyerBalance.getRecord().getBuyerBalanceChangeType());
        assertEquals(remark, buyerBalance.getRecord().getRemark());

        // 验证数据库中的余额是否已保存
        buyerBalance = buyerBalanceDomainService.getByBuyerId(buyerId);
        assertNotNull(buyerBalance);
        assertEquals(0, balance.compareTo(buyerBalance.getBalance()));
    }

    @Test
    void testRecharge_noBalanceBuyer() {
        long buyerId = noBalanceBuyerId;

        // 第一次充值
        buyerBalanceDomainService.recharge(buyerId, new BigDecimal("50"), BuyerBalanceChangeType.Recharge, "第一次充值");

        // 第二次充值
        BigDecimal amount = new BigDecimal("150");
        BuyerBalanceChangeType buyerBalanceChangeType = BuyerBalanceChangeType.Recharge;
        String remark = "第二次充值";

        BuyerBalance result = buyerBalanceDomainService.recharge(buyerId, amount, buyerBalanceChangeType, remark);

        assertNotNull(result);
        assertEquals(0, new BigDecimal("200").compareTo(result.getBalance()));

        assertNotNull(result.getRecord());
        assertEquals(buyerId, result.getRecord().getBuyerId());
        assertEquals(0, amount.compareTo(result.getRecord().getAmount()));
        assertEquals(buyerBalanceChangeType, result.getRecord().getBuyerBalanceChangeType());
        assertEquals(remark, result.getRecord().getRemark());
    }

    @Test
    void testRecharge_notfoundBuyer() {
        long buyerId = notfoundBuyerId;

        // 第一次充值
        assertThrows(RuntimeException.class, ()-> buyerBalanceDomainService.recharge(buyerId, new BigDecimal("50"), BuyerBalanceChangeType.Recharge, "第一次充值") );
    }

}
