package com.yabobaozb.ecom.commodity.infra.mapper;

import com.yabobaozb.ecom.commodity.infra.model.SkuInventoryDO;
import com.yabobaozb.ecom.commodity.infra.model.SkuInventoryVersionDO;

import java.util.List;

public interface SkuInventoryDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sku_inventory
     *
     * @mbg.generated Thu Jun 19 11:34:39 CST 2025
     */
    int deleteByPrimaryKey(Long skuId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sku_inventory
     *
     * @mbg.generated Thu Jun 19 11:34:39 CST 2025
     */
    int insert(SkuInventoryDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sku_inventory
     *
     * @mbg.generated Thu Jun 19 11:34:39 CST 2025
     */
    int insertSelective(SkuInventoryDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sku_inventory
     *
     * @mbg.generated Thu Jun 19 11:34:39 CST 2025
     */
    SkuInventoryDO selectByPrimaryKey(Long skuId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sku_inventory
     *
     * @mbg.generated Thu Jun 19 11:34:39 CST 2025
     */
    int updateByPrimaryKeySelective(SkuInventoryDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sku_inventory
     *
     * @mbg.generated Thu Jun 19 11:34:39 CST 2025
     */
    int updateByPrimaryKey(SkuInventoryDO record);

    int updateInventoryByVersion(SkuInventoryVersionDO record);

    List<SkuInventoryDO> selectBySkuIds(List<Long> skuIdList);
}