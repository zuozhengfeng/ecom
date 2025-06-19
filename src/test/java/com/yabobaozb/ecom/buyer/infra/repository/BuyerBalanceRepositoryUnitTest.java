package com.yabobaozb.ecom.buyer.infra.repository;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.infra.mapper.BuyerBalanceDOMapper;
import com.yabobaozb.ecom.buyer.infra.model.BuyerBalanceDO;
import com.yabobaozb.ecom.buyer.infra.model.BuyerBalanceVersionDO;
import com.yabobaozb.ecom.buyer.infra.repository.impl.BuyerBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuyerBalanceRepositoryUnitTest {

    @InjectMocks
    BuyerBalanceRepository buyerBalanceRepository;

    @Mock
    BuyerBalanceDOMapper buyerBalanceDOMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks( this);
    }

    private final long buyerId = 10001L;

    @Test
    public void testGetByBuyerId_WhenRecordNotFound() {
        when(buyerBalanceDOMapper.selectByPrimaryKey(buyerId)).thenReturn(null);

        BuyerBalance result = buyerBalanceRepository.getByBuyerId(buyerId);

        assertNull(result);
        verify(buyerBalanceDOMapper, times(1)).selectByPrimaryKey(buyerId);
        verifyNoMoreInteractions(buyerBalanceDOMapper);
    }

    @Test
    public void testGetByBuyerId_WhenRecordFound() {
        BigDecimal balance = new BigDecimal("100.00");
        long version = 1L;

        BuyerBalanceDO buyerBalanceDO = new BuyerBalanceDO();
        buyerBalanceDO.setBuyerId(buyerId);
        buyerBalanceDO.setBalance(balance);
        buyerBalanceDO.setVersion(version);
        buyerBalanceDO.setCreateTime(new Date());
        buyerBalanceDO.setUpdateTime(new Date());

        when(buyerBalanceDOMapper.selectByPrimaryKey(buyerId)).thenReturn(buyerBalanceDO);

        BuyerBalance result = buyerBalanceRepository.getByBuyerId(buyerId);

        assertNotNull(result);
        assertEquals(buyerId, result.getBuyerId());
        assertEquals(balance, result.getBalance());
        assertEquals(version, result.getVersion());
        verify(buyerBalanceDOMapper, times(1)).selectByPrimaryKey(buyerId);
        verifyNoMoreInteractions(buyerBalanceDOMapper);
    }

    @Test
    public void testRefreshBalance_WhenRecordNotFound() {
        BuyerBalance buyerBalance = new BuyerBalance(buyerId, BigDecimal.valueOf(100));

        when(buyerBalanceDOMapper.insertSelective(any(BuyerBalanceDO.class))).thenReturn(1);

        buyerBalance.refreshVersion();
        buyerBalanceRepository.refreshBalance(buyerBalance);

        verify(buyerBalanceDOMapper, times(1)).insertSelective(any(BuyerBalanceDO.class));
        verifyNoMoreInteractions(buyerBalanceDOMapper);
    }
    @Test
    public void testRefreshBalance_WhenRecordFound() {
        BuyerBalance buyerBalance = new BuyerBalance(buyerId, BigDecimal.valueOf(100), 1L);

        when(buyerBalanceDOMapper.updateBalanceByVersion(any(BuyerBalanceVersionDO.class))).thenReturn(1);

        buyerBalance.refreshVersion();
        buyerBalanceRepository.refreshBalance(buyerBalance);

        verify(buyerBalanceDOMapper, times(1)).updateBalanceByVersion(any(BuyerBalanceVersionDO.class));
        verifyNoMoreInteractions(buyerBalanceDOMapper);
    }

}
