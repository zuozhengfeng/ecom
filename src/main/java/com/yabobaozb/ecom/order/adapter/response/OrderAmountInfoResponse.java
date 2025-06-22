package com.yabobaozb.ecom.order.adapter.response;

import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.infra.enums.PayResult;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderAmountInfoResponse(@Getter long orderId, @Getter long merchantId, @Getter BigDecimal totalAmount,
                                      @Getter PayResult payResult, @Getter LocalDateTime createTime) {

    public static class Converter {
        public static OrderAmountInfoResponse convertToResponse(OrderInfo orderInfo) {
            return new OrderAmountInfoResponse(orderInfo.getOrderId(), orderInfo.getMerchantId(), orderInfo.getTotalAmount(), orderInfo.getPayResult(), orderInfo.getCreateTime());
        }
    }

}
