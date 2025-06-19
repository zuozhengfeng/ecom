package com.yabobaozb.ecom.buyer.infra.repository;

import com.yabobaozb.ecom.buyer.domain.BuyerBalance;

public interface IBuyerBalanceRepository {

    /** 刷新余额，余额数据存在就乐观锁更新，不存在就插入数据*/
    void refreshBalance(BuyerBalance buyerBalance);

    /** 获取用户余额，余额数据存在就返回，不存在就返回null */
    BuyerBalance getByBuyerId(long buyerId);
}
