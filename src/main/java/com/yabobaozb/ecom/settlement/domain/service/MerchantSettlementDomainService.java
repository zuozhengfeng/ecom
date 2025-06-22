package com.yabobaozb.ecom.settlement.domain.service;

import com.yabobaozb.ecom.merchant.adapter.local.MerchantLocalAdapter;
import com.yabobaozb.ecom.merchant.adapter.response.MerchantBalanceChangeResponse;
import com.yabobaozb.ecom.order.adapter.local.OrderLocalAdapter;
import com.yabobaozb.ecom.order.adapter.response.OrderAmountInfoResponse;
import com.yabobaozb.ecom.settlement.domain.MerchantDailySettlement;
import com.yabobaozb.ecom.settlement.infra.repository.ISettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MerchantSettlementDomainService {

    private final OrderLocalAdapter orderLocalAdapter;

    private final MerchantLocalAdapter merchantLocalAdapter;

    private final ISettlementRepository settlementRepository;

    @Autowired
    public MerchantSettlementDomainService(OrderLocalAdapter orderLocalAdapter, MerchantLocalAdapter merchantLocalAdapter, ISettlementRepository settlementRepository) {
        this.orderLocalAdapter = orderLocalAdapter;
        this.merchantLocalAdapter = merchantLocalAdapter;
        this.settlementRepository = settlementRepository;
    }

    // 执行按日结算
    public MerchantDailySettlement executeDailySettlement(long merchantId, String settleTime, LocalDateTime beginAt, LocalDateTime endAt) {

        // 获取商家的订单记录
        // 注意：简化处理，没有考虑取消订单和退款的情况。
        List<OrderAmountInfoResponse> orders = orderLocalAdapter.listOrdersByMerchantAndCreateTime(merchantId, beginAt, endAt);

        // 获取商家的帐户变动记录
        // 注意：没有加商户余额的快照，简化处理。第一条变动记录是开始时间的快照，最后一条变动记录是结束时间的快照
        List<MerchantBalanceChangeResponse> merchantBalanceChanges = merchantLocalAdapter.listBalanceChangesByMerchantId(merchantId, beginAt, endAt);

        // 结算结果
        // 1、均无记录直接返回空结算结果报告
        // 2、存在任意一类记录，比较双方的金额差异
        MerchantDailySettlement merchantDailySettlement;
        if ( orders.size() == 0 && merchantBalanceChanges.size() == 0 ) {
            merchantDailySettlement = MerchantDailySettlement.buildEmptyRecordReport(merchantId, settleTime, beginAt, endAt);
        }
        else {
            // 订单的记录金额
            BigDecimal orderAmount = orders.size()==0 ? BigDecimal.ZERO : orders.stream().map(OrderAmountInfoResponse::getTotalAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

            // 商家收益的金额
            BigDecimal merchantIncreaseAmount = merchantBalanceChanges.size()==0 ? BigDecimal.ZERO :
                    merchantBalanceChanges.get(merchantBalanceChanges.size()-1).getBalanceTo().subtract(merchantBalanceChanges.get(0).getBalanceFrom());

            merchantDailySettlement = MerchantDailySettlement.buildNormalRecordReport(merchantId, settleTime, beginAt, endAt, orderAmount, merchantIncreaseAmount);
        }

        // 存储
        settlementRepository.saveSettlement(merchantDailySettlement);

        return merchantDailySettlement;
    }
}
