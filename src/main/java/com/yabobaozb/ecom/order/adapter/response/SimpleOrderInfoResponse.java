package com.yabobaozb.ecom.order.adapter.response;

import com.yabobaozb.ecom.order.domain.OrderInfo;
import lombok.Getter;


public class SimpleOrderInfoResponse {

    @Getter
    private final long orderId;

    // 添加更多

    public SimpleOrderInfoResponse(long orderId) {
        this.orderId = orderId;
    }

    public static class Converter {

        public static SimpleOrderInfoResponse convert(OrderInfo orderInfo) {
            return new SimpleOrderInfoResponse( orderInfo.getOrderId() );
        }

    }
}
