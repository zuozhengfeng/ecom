package com.yabobaozb.ecom.order.adapter.local;

import com.google.common.collect.Lists;
import com.yabobaozb.ecom.order.adapter.response.OrderAmountInfoResponse;
import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.domain.service.OrderDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class OrderLocalAdapter {

    private final OrderDomainService orderDomainService;

    @Autowired
    public OrderLocalAdapter(OrderDomainService orderDomainService) {
        this.orderDomainService = orderDomainService;
    }

    public List<OrderAmountInfoResponse> listOrdersByMerchantAndCreateTime(long merchantId, LocalDateTime beginAt, LocalDateTime endAt) {
        List<OrderInfo> orderList = orderDomainService.listOrdersByMerchantAndCreateTime(merchantId, beginAt, endAt);
        if ( orderList.size() == 0 ) {
            return Lists.newArrayList();
        }
        return orderList.stream().map(OrderAmountInfoResponse.Converter::convertToResponse).collect(Collectors.toList());
    }
}
