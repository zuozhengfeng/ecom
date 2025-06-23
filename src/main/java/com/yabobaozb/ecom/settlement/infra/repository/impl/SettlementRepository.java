package com.yabobaozb.ecom.settlement.infra.repository.impl;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlementRecord;
import com.yabobaozb.ecom.settlement.infra.mapper.SettlementInfoDOMapper;
import com.yabobaozb.ecom.settlement.infra.mapper.SettlementInfoRecordDOMapper;
import com.yabobaozb.ecom.settlement.infra.model.SettlementInfoDO;
import com.yabobaozb.ecom.settlement.infra.model.SettlementInfoRecordDO;
import com.yabobaozb.ecom.settlement.infra.model.converter.SettlementInfoConverter;
import com.yabobaozb.ecom.settlement.infra.model.converter.SettlementInfoRecordConverter;
import com.yabobaozb.ecom.settlement.infra.repository.ISettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettlementRepository implements ISettlementRepository {

    private final SettlementInfoDOMapper settlementInfoDOMapper;

    private final SettlementInfoRecordDOMapper settlementInfoRecordDOMapper;

    @Autowired
    public SettlementRepository(SettlementInfoDOMapper settlementInfoDOMapper, SettlementInfoRecordDOMapper settlementInfoRecordDOMapper) {
        this.settlementInfoDOMapper = settlementInfoDOMapper;
        this.settlementInfoRecordDOMapper = settlementInfoRecordDOMapper;
    }
    @Override
    public void saveSettlement(MerchantDailySettlement merchantDailySettlement) {
        int executeCount;
        if ( -1 == merchantDailySettlement.getVersion() ) {
            executeCount = settlementInfoDOMapper.insertSelective(SettlementInfoConverter.convertToDO(merchantDailySettlement));
        }
        else {
            executeCount = settlementInfoDOMapper.updateByVersion(SettlementInfoConverter.convertToVersionDO(merchantDailySettlement));
        }
        if ( executeCount <= 0 ) {
            throw new RuntimeException("商户结算记录更新失败");
        }
        executeCount = settlementInfoRecordDOMapper.insertSelective(SettlementInfoRecordConverter.convertToDO(merchantDailySettlement.getSettlementRecord()));
        if ( executeCount <= 0 ) {
            throw new RuntimeException("商户结算记录更新失败");
        }
    }

    @Override
    public MerchantDailySettlement getByMerchantAndSettleTime(long merchantId, String settleTime) {
        List<SettlementInfoDO> settlementInfoDOs = settlementInfoDOMapper.selectByMerchantAndSettleTime(merchantId, settleTime);
        if ( 0 == settlementInfoDOs.size() ) {
            return null;
        }
        if ( settlementInfoDOs.size() > 1 ) {
            throw new RuntimeException("商户结算记录重复");
        }
        return SettlementInfoConverter.convertToAggregate(settlementInfoDOs.get(0));
    }

    @Override
    public MerchantDailySettlementRecord getRecentRecordByMerchantAndSettleTime(long merchantId, String settleTime) {
        List<SettlementInfoRecordDO> settlementInfoRecordDOs = settlementInfoRecordDOMapper.selectByMerchantAndSettleTime(merchantId, settleTime);
        if ( null == settlementInfoRecordDOs ) {
            return null;
        }
        SettlementInfoRecordDO settlementInfoRecordDO = settlementInfoRecordDOs.get(settlementInfoRecordDOs.size()-1);
        return SettlementInfoRecordConverter.convertToAggregate(settlementInfoRecordDO);
    }
}
