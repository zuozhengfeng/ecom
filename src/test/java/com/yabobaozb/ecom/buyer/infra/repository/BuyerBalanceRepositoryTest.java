package com.yabobaozb.ecom.buyer.infra.repository;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import com.yabobaozb.ecom.buyer.infra.mapper.BuyerBalanceDOMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@SpringBootTest
public class BuyerBalanceRepositoryTest {

    @Autowired
    IBuyerBalanceRepository buyerBalanceRepository;

    @Autowired
    BuyerBalanceDOMapper buyerBalanceDOMapper;

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {

    }

    private final long normalBuyerId = 10000L;
    private final long noBalanceBuyerId = 10001L;
    private final long notfoundBuyerId = 10002L;

    // truncate table ecom.buyer_balance;
    // select * from ecom.buyer_balance;

    @Test
    public void testRefreshBalance_HasBalanceBuyer() {
        BuyerBalance buyerBalance = buyerBalanceRepository.getByBuyerId(normalBuyerId);
        assertNotNull(buyerBalance);

        BigDecimal amount = BigDecimal.valueOf(51);
        BigDecimal balance = amount.add(buyerBalance.getBalance());
        buyerBalance.increase(amount, BuyerBalanceChangeType.Recharge, "用户充值");

        buyerBalance.refreshVersion();
        buyerBalanceRepository.refreshBalance(buyerBalance);

        buyerBalance = buyerBalanceRepository.getByBuyerId(normalBuyerId);
        assertEquals(buyerBalance.getVersion(), 1L);
        assertEquals(0, buyerBalance.getBalance().compareTo(balance));
    }



    @Test
    public void testRefreshBalance_NoBalanceBuyer() {
        BuyerBalance buyerBalance = buyerBalanceRepository.getByBuyerId(noBalanceBuyerId);
        assertNull(buyerBalance);
        buyerBalance = new BuyerBalance(noBalanceBuyerId, BigDecimal.ZERO);

        BigDecimal amount = BigDecimal.valueOf(51);
        BigDecimal balance = amount.add(buyerBalance.getBalance());
        buyerBalance.increase(amount, BuyerBalanceChangeType.Recharge, "用户充值");

        buyerBalance.refreshVersion();
        buyerBalanceRepository.refreshBalance(buyerBalance);

        buyerBalance = buyerBalanceRepository.getByBuyerId(noBalanceBuyerId);
        assertNotNull(buyerBalance);
        assertEquals(buyerBalance.getVersion(), 0L);
        assertEquals(0, buyerBalance.getBalance().compareTo(balance));
    }

    @Test
    public void testRefreshBalance_NotfoundBuyer() {
        BigDecimal balance = BigDecimal.valueOf(100);
        BuyerBalance buyerBalance = new BuyerBalance(noBalanceBuyerId, balance);
        buyerBalance.refreshVersion();
        assertThrows(RuntimeException.class, () -> buyerBalanceRepository.refreshBalance(buyerBalance));
    }


    @Test
    public void testGetByBuyerId_NormalBuyer() {
        BuyerBalance buyerBalance = buyerBalanceRepository.getByBuyerId(normalBuyerId);
        assertNotNull(buyerBalance);
    }

    @Test
    public void testGetByBuyerId_NoBalanceBuyer() {
        BuyerBalance buyerBalance = buyerBalanceRepository.getByBuyerId(noBalanceBuyerId);
        assertNull(buyerBalance);
    }

    @Test
    public void testGetByBuyerId_NotfoundBuyer() {
        assertThrows(RuntimeException.class, () -> buyerBalanceRepository.getByBuyerId(notfoundBuyerId));
    }

}
