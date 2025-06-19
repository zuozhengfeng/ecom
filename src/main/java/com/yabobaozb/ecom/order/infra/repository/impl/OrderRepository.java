package com.yabobaozb.ecom.order.infra.repository.impl;

import com.google.common.collect.Lists;
import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.domain.OrderItem;
import com.yabobaozb.ecom.order.domain.OrderPayment;
import com.yabobaozb.ecom.order.infra.mapper.OrderInfoDOMapper;
import com.yabobaozb.ecom.order.infra.mapper.OrderItemDOMapper;
import com.yabobaozb.ecom.order.infra.mapper.OrderPaymentDOMapper;
import com.yabobaozb.ecom.order.infra.model.OrderInfoDO;
import com.yabobaozb.ecom.order.infra.model.OrderItemDO;
import com.yabobaozb.ecom.order.infra.model.OrderPaymentDO;
import com.yabobaozb.ecom.order.infra.model.converter.OrderInfoConverter;
import com.yabobaozb.ecom.order.infra.model.converter.OrderItemConverter;
import com.yabobaozb.ecom.order.infra.model.converter.OrderPaymentConverter;
import com.yabobaozb.ecom.order.infra.repository.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderRepository implements IOrderRepository {

    private final OrderInfoDOMapper orderInfoDOMapper;
    private final OrderItemDOMapper orderItemDOMapper;

    private final OrderPaymentDOMapper orderPaymentDOMapper;

    @Autowired
    public OrderRepository(OrderInfoDOMapper orderInfoDOMapper, OrderItemDOMapper orderItemDOMapper, OrderPaymentDOMapper orderPaymentDOMapper) {
        this.orderInfoDOMapper = orderInfoDOMapper;
        this.orderItemDOMapper = orderItemDOMapper;
        this.orderPaymentDOMapper = orderPaymentDOMapper;
    }

    @Override
    public void saveOrder(OrderInfo orderInfo) {

        // 存储订单
        orderInfoDOMapper.insertSelective( OrderInfoConverter.convertToDO(orderInfo) );

        // 存储订单项目
        for (OrderItem orderItem : orderInfo.getOrderItems()) {
            orderItemDOMapper.insertSelective( OrderItemConverter.convertToDO(orderItem) );
        }

    }

    @Override
    public Optional<OrderInfo> getById(long orderId) {
        OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(orderId);
        if ( null == orderInfoDO ) {
            return Optional.empty();
        }
        OrderInfo orderInfo = OrderInfoConverter.convertToAggregate(orderInfoDO);
        return Optional.of(orderInfo);
    }

    @Override
    public List<OrderPayment> listPaymentsByOrderId(long orderId) {
        List<OrderPaymentDO> orderPaymentDOList = orderPaymentDOMapper.selectByOrderId(orderId);
        if ( 0 == orderPaymentDOList.size() ) {
            return Lists.newArrayList();
        }
        return orderPaymentDOList.stream().map(OrderPaymentConverter::convertToAggregate).toList();
    }

    @Override
    public List<OrderItem> listItemsByOrderId(long orderId) {
        List<OrderItemDO> orderItemDOList = orderItemDOMapper.selectByOrderId(orderId);
        if ( 0 == orderItemDOList.size() ) {
            return Lists.newArrayList();
        }
        return orderItemDOList.stream().map(OrderItemConverter::convertToAggregate).toList();
    }

    @Override
    public void refreshPayResult(OrderInfo orderInfo) {

        // 更新订单的支付状态
        orderInfoDOMapper.updateByPrimaryKeySelective( OrderInfoConverter.convertToDOOnlyOrderStatus(orderInfo) );

        // 增加订单的支付记录
        for (OrderPayment orderPayment : orderInfo.getOrderPayments()) {
            orderPaymentDOMapper.insertSelective( OrderPaymentConverter.convertToDO(orderPayment) );
        }

    }


}
