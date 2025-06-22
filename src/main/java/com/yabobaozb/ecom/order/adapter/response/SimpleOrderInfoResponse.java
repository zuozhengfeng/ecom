package com.yabobaozb.ecom.order.adapter.response;

import com.yabobaozb.ecom.order.domain.OrderInfo;
import lombok.Getter;


public record SimpleOrderInfoResponse(@Getter long orderId) {

    // 添加更多

    public static class Converter {

        public static SimpleOrderInfoResponse convert(OrderInfo orderInfo) {
            return new SimpleOrderInfoResponse(orderInfo.getOrderId());
        }

    }
}
