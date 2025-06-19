package com.yabobaozb.ecom.commodity.infra.repository;

import com.yabobaozb.ecom.commodity.domain.SkuInfo;
import com.yabobaozb.ecom.commodity.domain.SkuInventory;

import java.util.List;

public interface ISkuRepository {

    SkuInventory getBySkuId(long skuId);

    List<SkuInfo> listSkuInfos(long merchantId, long[] skuIds);

    void refreshInventory(SkuInventory skuInventory);
}
