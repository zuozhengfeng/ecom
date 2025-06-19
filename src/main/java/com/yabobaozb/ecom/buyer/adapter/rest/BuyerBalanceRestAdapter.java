package com.yabobaozb.ecom.buyer.adapter.rest;

import com.yabobaozb.ecom.buyer.adapter.response.SimpleBuyerBalanceResponse;
import com.yabobaozb.ecom.buyer.domain.command.BuyerBalanceRechargeCommand;
import com.yabobaozb.ecom.buyer.domain.service.BuyerBalanceDomainService;
import com.yabobaozb.ecom.buyer.infra.enums.BuyerBalanceChangeType;
import com.yabobaozb.ecom.common.ReturnResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer/balance")
public class BuyerBalanceRestAdapter {
    private final BuyerBalanceDomainService buyerBalanceDomainService;

    @Autowired
    public BuyerBalanceRestAdapter(BuyerBalanceDomainService buyerBalanceDomainService) {
        this.buyerBalanceDomainService = buyerBalanceDomainService;
    }

    /** 用户充值 */
    @PostMapping("/recharge")
    public ReturnResult<SimpleBuyerBalanceResponse> recharge(@RequestParam long buyerId, @RequestParam String amount) {
        ReturnResult<SimpleBuyerBalanceResponse> result = new ReturnResult<>();
        BuyerBalanceRechargeCommand command = new BuyerBalanceRechargeCommand(buyerId, amount, BuyerBalanceChangeType.Recharge, "充值", buyerBalanceDomainService);
        result.setData(SimpleBuyerBalanceResponse.Converter.convert(command.execute()));
        return result;
    }
}
