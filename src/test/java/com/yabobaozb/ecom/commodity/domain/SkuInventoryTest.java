package com.yabobaozb.ecom.commodity.domain;

import com.yabobaozb.ecom.commodity.infra.enums.SkuInventoryChangeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SkuInventoryTest {

    private SkuInventory skuInventory;

    // 不使用H2数据库
    private static final long skuId = 30000001L;
    private static final int initialInventory = 10;

    @BeforeEach
    public void setUp() {
        skuInventory = new SkuInventory(skuId, initialInventory);
    }

    /**
     * 正常增加库存
     */
    @Test
    public void testIncrease_Normal() {

        int quantity = 5;
        SkuInventoryChangeType changeType = SkuInventoryChangeType.MerchantIncrease;
        String remark = "test remark";

        skuInventory.increase(quantity, changeType, remark);

        // 验证库存是否正确更新
        int expectedInventory = initialInventory + quantity;
        assertEquals(expectedInventory, skuInventory.getAvailableInventory());

        // 验证记录是否生成
        SkuInventoryRecord record = skuInventory.getRecord();
        assertNotNull(record);
        assertEquals(skuId, record.getSkuId());
        assertEquals(changeType, record.getChangeType());
        assertEquals(initialInventory, record.getInventoryFrom());
        assertEquals(expectedInventory, record.getInventoryTo());
        assertEquals(quantity, record.getQuantity());
        assertEquals(remark, record.getRemark());
    }

    /**
     * 数量为0，应抛出异常
     */
    @Test
    public void testIncrease_WithZeroQuantity_ShouldThrowException() {
        int quantity = 0;
        String remark = "test remark";
        assertThrows(RuntimeException.class, () -> skuInventory.increase(quantity, SkuInventoryChangeType.MerchantIncrease, remark));
    }

    /**
     * 数量为负数，应抛出异常
     */
    @Test
    public void testIncrease_WithNegativeQuantity_ShouldThrowException() {
        int quantity = -5;
        String remark = "test remark";
        assertThrows(RuntimeException.class, () -> skuInventory.increase(quantity, SkuInventoryChangeType.MerchantIncrease, remark));
    }

    /**
     * changeType 为 null，应抛出异常
     */
    @Test
    public void testIncrease_WithNullChangeType_ShouldProceedNormally() {
        int quantity = 5;
        String remark = "test remark";
        assertThrows(RuntimeException.class, () -> skuInventory.increase(quantity, null, remark));

    }

    /**
     * remark 为 null，允许通过
     */
    @Test
    public void testIncrease_WithNullRemark_ShouldProceedNormally() {
        skuInventory.increase(5, SkuInventoryChangeType.MerchantIncrease, null);

        SkuInventoryRecord record = skuInventory.getRecord();
        assertNotNull(record);
        assertNull(record.getRemark());
    }


    /**
     * 正常减少库存
     */
    @Test
    public void testDecrease_Normal() {
        int quantity = 5;
        SkuInventoryChangeType changeType = SkuInventoryChangeType.BuyerConsume;
        String remark = "test remark";

        skuInventory.decrease(quantity, changeType, remark);

        // 验证库存是否正确更新
        int expectedInventory = initialInventory - quantity;
        assertEquals(expectedInventory, skuInventory.getAvailableInventory());

        // 验证记录是否生成
        SkuInventoryRecord record = skuInventory.getRecord();
        assertNotNull(record);
        assertEquals(skuId, record.getSkuId());
        assertEquals(changeType, record.getChangeType());
        assertEquals(initialInventory, record.getInventoryFrom());
        assertEquals(expectedInventory, record.getInventoryTo());
        assertEquals(quantity, record.getQuantity());
        assertEquals(remark, record.getRemark());
    }

    /**
     * 数量为0，应抛出异常
     */
    @Test
    public void testDecrease_WithZeroQuantity_ShouldThrowException() {
        int quantity = 0;
        String remark = "test remark";
        assertThrows(RuntimeException.class, () ->
                skuInventory.decrease(quantity, SkuInventoryChangeType.BuyerConsume, remark));
    }

    /**
     * 数量为负数，应抛出异常
     */
    @Test
    public void testDecrease_WithNegativeQuantity_ShouldThrowException() {
        int quantity = -5;
        String remark = "test remark";
        assertThrows(RuntimeException.class, () ->
                skuInventory.decrease(quantity, SkuInventoryChangeType.BuyerConsume, remark));
    }

    /**
     * changeType 为 null，应抛出异常
     */
    @Test
    public void testDecrease_WithNullChangeType_ShouldThrowException() {
        int quantity = 5;
        String remark = "test remark";
        assertThrows(RuntimeException.class, () ->
                skuInventory.decrease(quantity, null, remark));
    }

    /**
     * remark 为 null，允许通过
     */
    @Test
    public void testDecrease_WithNullRemark_ShouldProceedNormally() {
        skuInventory.decrease(5, SkuInventoryChangeType.BuyerConsume, null);

        SkuInventoryRecord record = skuInventory.getRecord();
        assertNotNull(record);
        assertNull(record.getRemark());
    }

    /**
     * 库存不足，应抛出异常
     */
    @Test
    public void testDecrease_WithInsufficientInventory_ShouldThrowException() {
        int quantity = 5 + initialInventory; // 大于初始库存
        String remark = "test remark";
        assertThrows(RuntimeException.class, () ->
                skuInventory.decrease(quantity, SkuInventoryChangeType.BuyerConsume, remark));
    }

}
