package com.yabobaozb.ecom.order.domain;

import com.google.common.collect.Lists;
import com.yabobaozb.ecom.buyer.adapter.response.SimpleBuyerBalanceResponse;
import com.yabobaozb.ecom.commodity.adapter.response.SimpleSkuInfoResponse;
import com.yabobaozb.ecom.common.infra.util.IDGenerator;
import com.yabobaozb.ecom.order.infra.enums.OrderStatus;
import com.yabobaozb.ecom.order.infra.enums.PayResult;
import com.yabobaozb.ecom.payment.infra.enums.PayType;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderInfo {

    @Getter
    private long orderId;

    @Getter
    private OrderStatus orderStatus;

    @Getter
    private PayResult payResult;

    @Getter
    private final long merchantId;

    @Getter
    private final long buyerId;

    @Getter
    private String remark;

    /** 买家帐户余额 */
    private SimpleBuyerBalanceResponse buyerBalance;

    /** 订单总金额 */
    @Getter
    private BigDecimal totalAmount;

    @Getter
    private int totalSkuSize;

    @Getter
    private int totalQuantity;

    /** 商品SKU库存信息 */
    private Map<Long, SimpleSkuInfoResponse> skuInventories;

    @Getter
    private long payId;

    @Getter
    private List<OrderItem> orderItems;

    @Getter
    private List<OrderPayment> orderPayments;

    @Getter
    private LocalDateTime createTime;

    public OrderInfo(long orderId, long buyerId, long merchantId, BigDecimal totalAmount, LocalDateTime createTime ) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.totalAmount = totalAmount;
        this.createTime = createTime;
    }

    public OrderInfo(long buyerId, long merchantId, long[] skuIds, int[] quantities, BigDecimal[] unitPrices, String remark, SimpleBuyerBalanceResponse buyerBalance, Map<Long, SimpleSkuInfoResponse> skuInventories) {
        Objects.requireNonNull(skuIds);
        Objects.requireNonNull(quantities);
        Objects.requireNonNull(unitPrices);
        Objects.requireNonNull(buyerBalance);
        Objects.requireNonNull(skuInventories);

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

        if (skuIds.length != skuInventories.size()) {
            throw new RuntimeException("参数错误");
        }

        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.remark = remark;
        this.buyerBalance = buyerBalance;
        this.skuInventories = skuInventories;


        this.totalSkuSize = skuIds.length;
        this.totalQuantity = Arrays.stream(quantities).sum();
        this.calculateTotalAmount(skuIds, quantities, unitPrices);
        this.buildOrderItems(skuIds, quantities, unitPrices);
    }

    /** 计算订单总金额 */
    private void calculateTotalAmount(long[] skuIds, int[] quantities, BigDecimal[] unitPrices) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (int i=0; i<skuIds.length; i++) {
            totalAmount = totalAmount.add( unitPrices[i].multiply(new BigDecimal(quantities[i])) );
        }
        this.totalAmount = totalAmount;
    }

    /** 构建订单明细 */
    private void buildOrderItems(long[] skuIds, int[] quantities, BigDecimal[] unitPrices) {
        List<OrderItem> orderItems = Lists.newArrayListWithCapacity(skuIds.length);

        for (int i=0; i<skuIds.length; i++) {
            String skuName = skuInventories.get(skuIds[i]).getSkuName();
            OrderItem orderItem = new OrderItem(this.orderId, skuIds[i], skuName, quantities[i], unitPrices[i]);
            orderItems.add(orderItem);
        }

        this.orderItems = orderItems;
    }

    /**
     * 创建订单
     * 检查用户帐户余额和库存，生成订单ID及明细
     */
    public void create() {
        this.checkOrderValid();

        // 验证通过生成订单相关信息
        this.orderId = generateOrderId();
        this.orderStatus = OrderStatus.CREATED;
        this.payResult = PayResult.UNPAID;
        refreshOrderIdForItems();
    }

    /**
     * 刷新订单ID
     */
    private void refreshOrderIdForItems() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(this.orderId);
        }
    }


    /** 验证订单创建 */
    protected void checkOrderValid() {

        // 买家余额足够
        checkBuyerBalance();

        // 商品库存足够
        checkSkuStocks();

        // 商品价格是否一致
        checkSkuPrices();

    }

    /** 验证买家余额 */
    protected void checkBuyerBalance() {
        if ( totalAmount.compareTo( buyerBalance.balance() ) > 0 ) {
            throw new RuntimeException("用户余额不足");
        }

    }

    /** 验证商品库存 */
    protected void checkSkuStocks() {

        for ( OrderItem orderItem : orderItems ) {
            SimpleSkuInfoResponse skuInfo = skuInventories.get(orderItem.getSkuId());
            if ( skuInfo.getAvailableInventory() < orderItem.getQuantity() ) {
                throw new RuntimeException("商品库存不足");
            }
        }

    }

    /** 验证商品价格 */
    protected void checkSkuPrices() {
        for ( OrderItem orderItem : orderItems ) {
            SimpleSkuInfoResponse skuInfo = skuInventories.get(orderItem.getSkuId());
            if ( 0 != skuInfo.getUnitPrice().compareTo(orderItem.getUnitPrice()) ) {
                throw new RuntimeException("商品价格不一致");
            }
        }
    }


    /** 生成订单ID */
    private long generateOrderId() {
        return IDGenerator.generateId();
    }

    public void refreshToPaid(long payId) {
        this.payId = payId;
        this.orderStatus = OrderStatus.PAID;

        // 暂时只支持1个支付, 支付方式固定为余额支付
        this.orderPayments = Lists.newArrayListWithCapacity(1);
        this.orderPayments.add(new OrderPayment(this.orderId, payId, this.totalAmount, PayType.BALANCE, PayResult.PAID));
    }


}
