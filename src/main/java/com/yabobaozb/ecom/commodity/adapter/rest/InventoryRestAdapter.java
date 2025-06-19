package com.yabobaozb.ecom.commodity.adapter.rest;

import com.yabobaozb.ecom.commodity.adapter.response.SimpleSkuInventoryResponse;
import com.yabobaozb.ecom.commodity.domain.service.InventoryDomainService;
import com.yabobaozb.ecom.common.ReturnResult;
import com.yabobaozb.ecom.commodity.domain.command.InventoryIncreaseCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commodity/inventory")
public class InventoryRestAdapter {


    private final InventoryDomainService inventoryDomainService;

    @Autowired
    public InventoryRestAdapter(InventoryDomainService inventoryDomainService) {
        this.inventoryDomainService = inventoryDomainService;
    }

    // 商家增加库存
    @PostMapping("/increase")
    public ReturnResult<SimpleSkuInventoryResponse> increase(@RequestParam long skuId, @RequestParam int quantity, @RequestParam String remark) {
        ReturnResult<SimpleSkuInventoryResponse> result = new ReturnResult<>();
        InventoryIncreaseCommand command = new InventoryIncreaseCommand(skuId, quantity, remark, inventoryDomainService);
        result.setData( SimpleSkuInventoryResponse.Converter.convert(command.execute()) );
        return result;
    }


}
