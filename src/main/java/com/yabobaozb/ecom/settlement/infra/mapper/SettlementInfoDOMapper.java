package com.yabobaozb.ecom.settlement.infra.mapper;

import com.yabobaozb.ecom.settlement.infra.model.SettlementInfoDO;
import com.yabobaozb.ecom.settlement.infra.model.SettlementInfoVersionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettlementInfoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table settlement_info
     *
     * @mbg.generated Sun Jun 22 17:08:30 CST 2025
     */
    int deleteByPrimaryKey(Long settleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table settlement_info
     *
     * @mbg.generated Sun Jun 22 17:08:30 CST 2025
     */
    int insert(SettlementInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table settlement_info
     *
     * @mbg.generated Sun Jun 22 17:08:30 CST 2025
     */
    int insertSelective(SettlementInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table settlement_info
     *
     * @mbg.generated Sun Jun 22 17:08:30 CST 2025
     */
    SettlementInfoDO selectByPrimaryKey(Long settleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table settlement_info
     *
     * @mbg.generated Sun Jun 22 17:08:30 CST 2025
     */
    int updateByPrimaryKeySelective(SettlementInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table settlement_info
     *
     * @mbg.generated Sun Jun 22 17:08:30 CST 2025
     */
    int updateByPrimaryKey(SettlementInfoDO record);

    List<SettlementInfoDO> selectByMerchantAndSettleTime(@Param("merchantId") long merchantId, @Param("settleTime") String settleTime);

    int updateByVersion(SettlementInfoVersionDO record);
}