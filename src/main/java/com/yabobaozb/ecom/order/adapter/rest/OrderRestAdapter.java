package com.yabobaozb.ecom.order.adapter.rest;

import com.yabobaozb.ecom.common.ReturnResult;
import com.yabobaozb.ecom.order.adapter.response.SimpleOrderInfoResponse;
import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.domain.command.OrderCreateCommand;
import com.yabobaozb.ecom.order.domain.service.OrderDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/order")
public class OrderRestAdapter {

    private final OrderDomainService orderDomainService;

    @Autowired
    public OrderRestAdapter(OrderDomainService orderDomainService) {
        this.orderDomainService = orderDomainService;
    }

    @PostMapping("/create")
    public ReturnResult<SimpleOrderInfoResponse> create(@RequestParam long buyerId,
                                                        @RequestParam long merchantId,
                                                        @RequestParam long[] skuIds,
                                                        @RequestParam int[] quantities,
                                                        @RequestParam String[] unitPrices,
                                                        @RequestParam String remark) {
        // 基础的校验
        checkCreateOrderParams(buyerId, merchantId, skuIds, quantities, unitPrices);

        ReturnResult<SimpleOrderInfoResponse> result = new ReturnResult<>();
        OrderCreateCommand command = new OrderCreateCommand(buyerId, merchantId, skuIds, quantities, unitPrices, remark, this.orderDomainService);
        OrderInfo orderInfo = command.execute();
        result.setData( SimpleOrderInfoResponse.Converter.convert(orderInfo) );
        return result;
    }

    private void checkCreateOrderParams(long buyerId, long merchantId, long[] skuIds, int[] quantities, String[] unitPrices) {
        Objects.requireNonNull(skuIds);
        Objects.requireNonNull(quantities);
        Objects.requireNonNull(unitPrices);

        if (buyerId <= 0) {
            throw new RuntimeException("参数错误");
        }

        if (merchantId <= 0) {
            throw new RuntimeException("参数错误");
        }

        if (skuIds.length == 0) {
            throw new RuntimeException("参数错误");
        }

        if (skuIds.length != quantities.length || skuIds.length != unitPrices.length) {
            throw new RuntimeException("参数错误");
        }
    }

}
