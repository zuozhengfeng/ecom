package com.yabobaozb.ecom.commodity.domain.service;

import com.yabobaozb.ecom.commodity.domain.SkuInfo;
import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import com.yabobaozb.ecom.commodity.infra.enums.SkuInventoryChangeType;
import com.yabobaozb.ecom.commodity.infra.repository.ISkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryDomainService {

    @Autowired
    private ISkuRepository inventoryRepository;

     public List<SkuInfo> listSkuInfos(long merchantId, long[] skuIds) {
        return inventoryRepository.listSkuInfos(merchantId, skuIds);
    }


    public SkuInventory decreaseInventory(long skuId, int quantity, String remark) {
        SkuInventory skuInventory = buildInventory(skuId);
        skuInventory.decrease(quantity, SkuInventoryChangeType.BuyerConsume, remark);
        skuInventory.refreshVersion();
        inventoryRepository.refreshInventory(skuInventory);
        return skuInventory;
    }


    @Transactional
    public SkuInventory increaseInventory(long skuId, int quantity, String remark) {
        SkuInventory skuInventory = buildInventory(skuId);
        skuInventory.increase(quantity, SkuInventoryChangeType.MerchantIncrease, remark);
        skuInventory.refreshVersion();
        inventoryRepository.refreshInventory(skuInventory);
        return skuInventory;
    }

    private SkuInventory buildInventory(long skuId) {
        SkuInventory skuInventory = inventoryRepository.getBySkuId(skuId);
        if ( null == skuInventory ) {
            skuInventory = new SkuInventory(skuId, 0);
        }
        return skuInventory;
    }

}
