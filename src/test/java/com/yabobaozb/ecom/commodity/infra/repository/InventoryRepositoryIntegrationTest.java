package com.yabobaozb.ecom.commodity.infra.repository;

import com.yabobaozb.ecom.commodity.domain.SkuInfo;
import com.yabobaozb.ecom.commodity.domain.SkuInventory;
import com.yabobaozb.ecom.commodity.infra.enums.SkuInventoryChangeType;
import com.yabobaozb.ecom.commodity.infra.mapper.SkuInfoDOMapper;
import com.yabobaozb.ecom.commodity.infra.repository.impl.SkuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class InventoryRepositoryIntegrationTest {

    @Autowired
    private SkuRepository inventoryRepository;

    @Autowired
    private SkuInfoDOMapper skuInfoDOMapper;

    final long normalSkuId = 30000L;
    final long noBalanceSkuId = 30001L;
    final long notFoundSkuId = 30002L;

    final long merchantId = 20000L;

    @BeforeEach
    public void setUp() {
        // 需要执行data.sql, 简单处理，不执行，单个执行测试用例
    }

    @Test
    void testRefreshInventory_NormalSku_UpdateCase() {
        long skuId = normalSkuId;

        SkuInventory skuInventory = inventoryRepository.getBySkuId(skuId);
        assertNotNull(skuInventory);
        int initialInventory = skuInventory.getAvailableInventory();
        int quantity = 10;
        int finalInventory = initialInventory + quantity;
        long finalVersion = 1 + skuInventory.getVersion();
        String remark = "test refresh inventory";

        skuInventory.increase(quantity, SkuInventoryChangeType.MerchantIncrease, remark);
        skuInventory.refreshVersion();
        inventoryRepository.refreshInventory(skuInventory);

        // 查询验证
        SkuInventory result = inventoryRepository.getBySkuId(skuId);
        assertThat(result).isNotNull();
        assertThat(result.getAvailableInventory()).isEqualTo(finalInventory);
        assertThat(result.getVersion()).isEqualTo(finalVersion);
    }

    @Test
    void testRefreshInventory_noBalanceSku_InsertCase() {
        long skuId = noBalanceSkuId;

        SkuInventory skuInventory = inventoryRepository.getBySkuId(skuId);
        assertNull(skuInventory); // 不存在
        skuInventory = new SkuInventory(skuId);
        int initialInventory = skuInventory.getAvailableInventory();
        int quantity = 10;
        int finalInventory = initialInventory + quantity;
        long finalVersion = 1 + skuInventory.getVersion();
        String remark = "test refresh inventory";

        skuInventory.increase(quantity, SkuInventoryChangeType.MerchantIncrease, remark);
        skuInventory.refreshVersion();
        inventoryRepository.refreshInventory(skuInventory);

        // 查询验证
        SkuInventory result = inventoryRepository.getBySkuId(skuId);
        assertThat(result).isNotNull();
        assertThat(result.getAvailableInventory()).isEqualTo(finalInventory);
        assertThat(result.getVersion()).isEqualTo(finalVersion);
    }

    @Test
    void testRefreshInventory_NotFoundSku() {
        long skuId = notFoundSkuId;
        // 这个方法不用测试
    }

    @Test
    void testGetBySkuId_NormalSku() {
        long skuId = normalSkuId;

        SkuInventory skuInventory = inventoryRepository.getBySkuId(skuId);
        assertNotNull(skuInventory);
    }

    @Test
    void testGetBySkuId_NoBalanceSku() {
        long skuId = noBalanceSkuId;

        SkuInventory skuInventory = inventoryRepository.getBySkuId(skuId);
        assertNull(skuInventory);
    }

    @Test
    void testGetBySkuId_NotFoundSku() {
        long skuId = notFoundSkuId;

        assertThrows(RuntimeException.class, () -> inventoryRepository.getBySkuId(skuId) );
    }




    @Test
    void testListBySkuIds_withValidSkus() {
        long[] skuIds = { normalSkuId };

        List<SkuInfo> result = inventoryRepository.listSkuInfos(merchantId, skuIds);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        SkuInfo inv1 = result.get(0);
        assertThat(inv1.getSkuId()).isEqualTo(normalSkuId);
        assertNotNull(inv1.getSkuInventory());
        assertThat(inv1.getSkuInventory().getAvailableInventory()).isEqualTo(99); // H2 data.sql初始的数据
    }

    @Test
    void testListBySkuIds_withNotFoundSku() {
        long[] skuIds = { notFoundSkuId };

        assertThrows(RuntimeException.class, () -> {
            inventoryRepository.listSkuInfos(merchantId, skuIds);
        });
    }

    @Test
    void testListBySkuIds_withMixedButValidSkus() {
        long[] skuIds = {normalSkuId, noBalanceSkuId }; // 部分存在，部分不存在

        List<SkuInfo> result = inventoryRepository.listSkuInfos(merchantId, skuIds);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
    }

    @Test
    void testListBySkuIds_withMixedButInvalidSkus() {
        long[] skuIds = {normalSkuId, noBalanceSkuId, notFoundSkuId }; // 部分存在，部分不存在

        assertThrows(RuntimeException.class, () -> {
            inventoryRepository.listSkuInfos(merchantId, skuIds);
        });
    }
}
