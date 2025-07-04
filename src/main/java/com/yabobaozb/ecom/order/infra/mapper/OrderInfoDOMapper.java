package com.yabobaozb.ecom.order.infra.mapper;

import com.yabobaozb.ecom.order.infra.model.OrderInfoDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderInfoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Fri Jun 20 18:17:14 CST 2025
     */
    int deleteByPrimaryKey(Long orderId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Fri Jun 20 18:17:14 CST 2025
     */
    int insert(OrderInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Fri Jun 20 18:17:14 CST 2025
     */
    int insertSelective(OrderInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Fri Jun 20 18:17:14 CST 2025
     */
    OrderInfoDO selectByPrimaryKey(Long orderId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Fri Jun 20 18:17:14 CST 2025
     */
    int updateByPrimaryKeySelective(OrderInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Fri Jun 20 18:17:14 CST 2025
     */
    int updateByPrimaryKey(OrderInfoDO record);

    List<OrderInfoDO> listByMerchantAndCreateTime(@Param("merchantId") long merchantId, @Param("beginAt") String beginAt, @Param("endAt") String endAt);
}