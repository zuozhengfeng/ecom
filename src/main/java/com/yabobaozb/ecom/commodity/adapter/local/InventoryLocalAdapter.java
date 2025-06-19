package com.yabobaozb.ecom.commodity.adapter.local;

import com.google.common.collect.Maps;
import com.yabobaozb.ecom.commodity.adapter.response.SimpleSkuInfoResponse;
import com.yabobaozb.ecom.commodity.adapter.response.SimpleSkuInventoryResponse;
import com.yabobaozb.ecom.commodity.domain.SkuInfo;
import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import com.yabobaozb.ecom.commodity.domain.command.InventoryDecreaseCommand;
import com.yabobaozb.ecom.commodity.domain.service.InventoryDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class InventoryLocalAdapter {

    @Autowired
    private InventoryDomainService inventoryDomainService;

    public Map<Long, SimpleSkuInfoResponse> listSkuInfos(long merchantId, long[] skuIds) {
        List<SkuInfo> skuInfoList = inventoryDomainService.listSkuInfos(merchantId, skuIds);
        Map<Long, SimpleSkuInfoResponse> result = Maps.newHashMap();
        skuInfoList.forEach(skuInfo -> {
            SimpleSkuInfoResponse resp = SimpleSkuInfoResponse.Converter.convert(skuInfo);
            result.put(skuInfo.getSkuId(), resp);
        });
        return result;
    }


    public void decreaseInventories(long[] skuIds, int[] quantities, String remark) {
        int len = skuIds.length;
        for (int i=0; i<len; i++) {
            InventoryDecreaseCommand command = new InventoryDecreaseCommand(skuIds[i], quantities[i], remark, inventoryDomainService);
            command.execute();
        }
    }
}
