package com.yabobaozb.ecom.commodity.infra.repository.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yabobaozb.ecom.commodity.domain.SkuInfo;
import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import com.yabobaozb.ecom.commodity.infra.mapper.SkuInfoDOMapper;
import com.yabobaozb.ecom.commodity.infra.mapper.SkuInventoryDOMapper;
import com.yabobaozb.ecom.commodity.infra.mapper.SkuInventoryRecordDOMapper;
import com.yabobaozb.ecom.commodity.infra.model.SkuInfoDO;
import com.yabobaozb.ecom.commodity.infra.model.SkuInventoryDO;
import com.yabobaozb.ecom.commodity.infra.model.converter.SkuInfoConverter;
import com.yabobaozb.ecom.commodity.infra.model.converter.SkuInventoryConverter;
import com.yabobaozb.ecom.commodity.infra.model.converter.SkuInventoryRecordConverter;
import com.yabobaozb.ecom.commodity.infra.repository.ISkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class SkuRepository implements ISkuRepository {

    private final SkuInfoDOMapper skuInfoDOMapper;
    private final SkuInventoryDOMapper skuInventoryDOMapper;

    private final SkuInventoryRecordDOMapper skuInventoryRecordDOMapper;

    @Autowired
    public SkuRepository(SkuInfoDOMapper skuInfoDOMapper, SkuInventoryDOMapper skuInventoryDOMapper, SkuInventoryRecordDOMapper skuInventoryRecordDOMapper) {
        this.skuInfoDOMapper = skuInfoDOMapper;
        this.skuInventoryDOMapper = skuInventoryDOMapper;
        this.skuInventoryRecordDOMapper = skuInventoryRecordDOMapper;
    }

    @Override
    public SkuInventory getBySkuId(long skuId) {
        // 商品必须存在
        checkSkuExisted(skuId);

        // 商品可以后面加
        SkuInventoryDO skuInventoryDO = skuInventoryDOMapper.selectByPrimaryKey(skuId);
        if ( null == skuInventoryDO ) {
            return null;
        }

        return SkuInventoryConverter.convertToAggregate(skuInventoryDO);
    }

    @Override
    public void refreshInventory(SkuInventory skuInventory) {
        // 商品必须存在
        checkSkuExisted(skuInventory.getSkuId());

        if ( skuInventory.getVersion() == -1 ) {
            skuInventoryDOMapper.insertSelective(SkuInventoryConverter.convertToDO(skuInventory));
        }
        else {
            skuInventoryDOMapper.updateInventoryByVersion(SkuInventoryConverter.convertToVersionDO(skuInventory));
        }

        // 增加变动记录
        skuInventoryRecordDOMapper.insertSelective(SkuInventoryRecordConverter.convertToDO(skuInventory.getRecord()));
    }

    @Override
    public List<SkuInfo> listSkuInfos(long merchantId, long[] skuIds) {
//        // 商品必须存在
//        Arrays.stream(skuIds).forEach(this::checkSkuExisted);
        List<Long> skuIdList = Arrays.stream(skuIds).boxed().toList();

        // 获取所有的商品信息
        List<SkuInfoDO> skuInfoDOList = skuInfoDOMapper.selectBySkuIds(merchantId, skuIdList);
        if ( skuInfoDOList.size() != skuIdList.size() ) {
            throw new RuntimeException("商品SKU不存在");
        }

        // 获取所有的库存信息
        List<SkuInventoryDO> skuInventoryDOList = skuInventoryDOMapper.selectBySkuIds(skuIdList);
        Map<Long, SkuInventoryDO> skuInventoryDOMap = Maps.newHashMapWithExpectedSize(skuInventoryDOList.size());
        skuInventoryDOList.forEach(skuInventoryDO -> skuInventoryDOMap.put(skuInventoryDO.getSkuId(), skuInventoryDO));

        // 转换
        List<SkuInfo> skuInfoList = Lists.newArrayListWithExpectedSize(skuInfoDOList.size());
        skuInfoDOList.forEach(skuInfoDO -> {
            SkuInventoryDO skuInventoryDO = skuInventoryDOMap.get(skuInfoDO.getSkuId());
            SkuInfo skuInfo = SkuInfoConverter.convertToAggregate(skuInfoDO, skuInventoryDO);
            skuInfoList.add(skuInfo);
        });
        return skuInfoList;
    }


    private void checkSkuExisted(long skuId) {
        SkuInfoDO skuInfo = skuInfoDOMapper.selectByPrimaryKey(skuId);
        if ( null == skuInfo ) {
            throw new RuntimeException("商品SKU不存在");
        }
    }
}
