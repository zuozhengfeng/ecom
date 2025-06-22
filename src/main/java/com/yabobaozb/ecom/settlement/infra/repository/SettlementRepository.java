package com.yabobaozb.ecom.settlement.infra.repository;

import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.infra.mapper.SettlementInfoDOMapper;
import com.yabobaozb.ecom.settlement.infra.model.converter.SettlementInfoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettlementRepository implements ISettlementRepository{

    private final SettlementInfoDOMapper settlementInfoDOMapper;

    @Autowired
    public SettlementRepository(SettlementInfoDOMapper settlementInfoDOMapper) {
        this.settlementInfoDOMapper = settlementInfoDOMapper;
    }
    @Override
    public void saveSettlement(MerchantDailySettlement merchantDailySettlement) {
        // TODO 未做去重等判断
        settlementInfoDOMapper.insertSelective(SettlementInfoConverter.convertToDO(merchantDailySettlement));
    }
}
