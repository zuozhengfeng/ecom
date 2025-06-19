package com.yabobaozb.ecom.order.domain.command;

import com.yabobaozb.ecom.common.ICommand;
import com.yabobaozb.ecom.order.domain.OrderInfo;
import com.yabobaozb.ecom.order.domain.service.OrderDomainService;
import lombok.Data;

import java.math.BigDecimal;

/** 订单创建命令 */
@Data
public class OrderCreateCommand implements ICommand<OrderInfo> {

    private final long buyerId;
    private final long merchantId;
    private final long[] skuIds;
    private final int[] quantities;
    private final BigDecimal[] unitPrices;
    private final String remark;

    private final OrderDomainService orderDomainService;

    public OrderCreateCommand(long buyerId, long merchantId, long[] skuIds, int[] quantities, String[] unitPrices, String remark, OrderDomainService orderDomainService) {
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.skuIds  = skuIds;
        this.quantities = quantities;

        BigDecimal[] bdUnitPrices = new BigDecimal[unitPrices.length];
        for (int i=0; i<unitPrices.length; i++) {
            bdUnitPrices[i] = new BigDecimal(unitPrices[i]);
        }
        this.unitPrices = bdUnitPrices;
        this.remark = remark;

        this.orderDomainService = orderDomainService;
    }

    @Override
    public OrderInfo execute() {
        return orderDomainService.createOrder(buyerId, merchantId, skuIds, quantities, unitPrices, remark);
    }
}
