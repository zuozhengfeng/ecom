package com.yabobaozb.ecom.buyer.infra.repository.impl;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;
import com.yabobaozb.ecom.buyer.infra.mapper.BuyerBalanceDOMapper;
import com.yabobaozb.ecom.buyer.infra.mapper.BuyerBalanceRecordDOMapper;
import com.yabobaozb.ecom.buyer.infra.mapper.BuyerInfoDOMapper;
import com.yabobaozb.ecom.buyer.infra.model.BuyerBalanceDO;
import com.yabobaozb.ecom.buyer.infra.model.BuyerInfoDO;
import com.yabobaozb.ecom.buyer.infra.model.converter.BuyerBalanceConverter;
import com.yabobaozb.ecom.buyer.infra.model.converter.BuyerBalanceRecordConverter;
import com.yabobaozb.ecom.buyer.infra.repository.IBuyerBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuyerBalanceRepository implements IBuyerBalanceRepository {
    private final BuyerInfoDOMapper buyerInfoDOMapper;
    private final BuyerBalanceDOMapper buyerBalanceDOMapper;
    private final BuyerBalanceRecordDOMapper buyerBalanceRecordDOMapper;

    @Autowired
    public BuyerBalanceRepository(BuyerInfoDOMapper buyerInfoDOMapper, BuyerBalanceDOMapper buyerBalanceDOMapper, BuyerBalanceRecordDOMapper buyerBalanceRecordDOMapper) {
        this.buyerInfoDOMapper = buyerInfoDOMapper;
        this.buyerBalanceDOMapper = buyerBalanceDOMapper;
        this.buyerBalanceRecordDOMapper = buyerBalanceRecordDOMapper;
    }

    @Override
    public void refreshBalance(BuyerBalance buyerBalance) {
        // 用户必须存在
        checkBuyerExisted(buyerBalance.getBuyerId());

        // 更改余额
        if ( buyerBalance.getVersion() == -1 ) {
            buyerBalanceDOMapper.insertSelective(BuyerBalanceConverter.convertToDO(buyerBalance));
        }
        else {
            buyerBalanceDOMapper.updateBalanceByVersion(BuyerBalanceConverter.convertToVersionDO(buyerBalance));
        }

        // 增加变动记录
        buyerBalanceRecordDOMapper.insertSelective(BuyerBalanceRecordConverter.convertToDO(buyerBalance.getRecord()));

    }

    @Override
    public BuyerBalance getByBuyerId(long buyerId) {
        // 用户必须存在
        checkBuyerExisted(buyerId);

        // 用户余额可以后面加
        BuyerBalanceDO buyerBalanceDO = buyerBalanceDOMapper.selectByPrimaryKey(buyerId);
        if ( null == buyerBalanceDO ) {
            return null;
        }

        return BuyerBalanceConverter.convertToAggregate(buyerBalanceDO);
    }

    private void checkBuyerExisted(long buyerId) {
        BuyerInfoDO buyerInfo = buyerInfoDOMapper.selectByPrimaryKey(buyerId);
        if ( null == buyerInfo ) {
            throw new RuntimeException("用户不存在");
        }
    }
}
