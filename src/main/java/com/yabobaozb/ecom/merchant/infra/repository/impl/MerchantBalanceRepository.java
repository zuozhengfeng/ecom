package com.yabobaozb.ecom.merchant.infra.repository.impl;

import com.google.common.collect.Lists;
import com.yabobaozb.ecom.merchant.domain.MerchantBalance;
import com.yabobaozb.ecom.merchant.domain.MerchantBalanceRecord;
import com.yabobaozb.ecom.merchant.infra.mapper.MerchantBalanceDOMapper;
import com.yabobaozb.ecom.merchant.infra.mapper.MerchantBalanceRecordDOMapper;
import com.yabobaozb.ecom.merchant.infra.mapper.MerchantInfoDOMapper;
import com.yabobaozb.ecom.merchant.infra.model.MerchantBalanceDO;
import com.yabobaozb.ecom.merchant.infra.model.MerchantBalanceRecordDO;
import com.yabobaozb.ecom.merchant.infra.model.MerchantInfoDO;
import com.yabobaozb.ecom.merchant.infra.model.converter.MerchantBalanceConverter;
import com.yabobaozb.ecom.merchant.infra.model.converter.MerchantBalanceRecordConverter;
import com.yabobaozb.ecom.merchant.infra.repository.IMerchantBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MerchantBalanceRepository implements IMerchantBalanceRepository {

    private final MerchantInfoDOMapper merchantInfoDOMapper;
    private final MerchantBalanceDOMapper merchantBalanceDOMapper;
    private final MerchantBalanceRecordDOMapper merchantBalanceRecordDOMapper;

    @Autowired
    public MerchantBalanceRepository(MerchantInfoDOMapper merchantInfoDOMapper, MerchantBalanceDOMapper merchantBalanceDOMapper, MerchantBalanceRecordDOMapper merchantBalanceRecordDOMapper) {
        this.merchantInfoDOMapper = merchantInfoDOMapper;
        this.merchantBalanceDOMapper = merchantBalanceDOMapper;
        this.merchantBalanceRecordDOMapper = merchantBalanceRecordDOMapper;
    }

    @Override
    public void refreshBalance(MerchantBalance merchantBalance) {
        // 商户必须存在
        checkMerchantExisted(merchantBalance.getMerchantId());

        // 更改余额
        if ( merchantBalance.getVersion() == -1 ) {
            merchantBalanceDOMapper.insertSelective(MerchantBalanceConverter.convertToDO(merchantBalance));
        }
        else {
            merchantBalanceDOMapper.updateBalanceByVersion(MerchantBalanceConverter.convertToVersionDO(merchantBalance));
        }

        // 增加变动记录
        merchantBalanceRecordDOMapper.insertSelective(MerchantBalanceRecordConverter.convertToDO(merchantBalance.getRecord()));

    }

    @Override
    public MerchantBalance getByMerchantId(long merchantId) {
        // 商户必须存在
        checkMerchantExisted(merchantId);

        // 商户余额可以后面加
        MerchantBalanceDO merchantBalanceDO = merchantBalanceDOMapper.selectByPrimaryKey(merchantId);
        if ( null == merchantBalanceDO ) {
            return null;
        }

        return MerchantBalanceConverter.convertToAggregate(merchantBalanceDO);
    }

    @Override
    public List<MerchantBalanceRecord> getRecordsByMerchantId(long merchantId) {
        List<MerchantBalanceRecordDO> merchantBalanceRecordDOList = merchantBalanceRecordDOMapper.selectByMerchantId(merchantId);
        if ( 0 == merchantBalanceRecordDOList.size() ) {
            return Lists.newArrayList();
        }
        List<MerchantBalanceRecord> resultList = Lists.newArrayListWithExpectedSize(merchantBalanceRecordDOList.size());
        merchantBalanceRecordDOList.forEach( merchantBalanceRecordDO -> resultList.add(MerchantBalanceRecordConverter.convertToAggregate(merchantBalanceRecordDO)) );
        return resultList;
    }

    private void checkMerchantExisted(long merchantId) {
        MerchantInfoDO merchantInfoDO = merchantInfoDOMapper.selectByPrimaryKey(merchantId);
        if ( null == merchantInfoDO ) {
            throw new RuntimeException("商家不存在");
        }
    }
}
