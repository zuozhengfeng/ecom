package com.yabobaozb.ecom.merchant.infra.mapper;

import com.yabobaozb.ecom.merchant.infra.model.MerchantBalanceDO;
import com.yabobaozb.ecom.merchant.infra.model.MerchantBalanceVersionDO;

public interface MerchantBalanceDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table merchant_balance
     *
     * @mbg.generated Thu Jun 19 10:51:38 CST 2025
     */
    int deleteByPrimaryKey(Long merchantId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table merchant_balance
     *
     * @mbg.generated Thu Jun 19 10:51:38 CST 2025
     */
    int insert(MerchantBalanceDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table merchant_balance
     *
     * @mbg.generated Thu Jun 19 10:51:38 CST 2025
     */
    int insertSelective(MerchantBalanceDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table merchant_balance
     *
     * @mbg.generated Thu Jun 19 10:51:38 CST 2025
     */
    MerchantBalanceDO selectByPrimaryKey(Long merchantId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table merchant_balance
     *
     * @mbg.generated Thu Jun 19 10:51:38 CST 2025
     */
    int updateByPrimaryKeySelective(MerchantBalanceDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table merchant_balance
     *
     * @mbg.generated Thu Jun 19 10:51:38 CST 2025
     */
    int updateByPrimaryKey(MerchantBalanceDO record);

    int updateBalanceByVersion(MerchantBalanceVersionDO record);
}