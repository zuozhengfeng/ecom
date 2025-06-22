
truncate table buyer_info;
truncate table buyer_balance;
truncate table buyer_balance_record;
truncate table merchant_info;
truncate table merchant_balance;
truncate table merchant_balance_record;
truncate table sku_info;
truncate table sku_inventory;
truncate table sku_inventory_record;
truncate table payment_info;
truncate table order_info;
truncate table order_item;
truncate table order_payment;

-- 用户10000有余额记录，用户10001没有余额记录
insert into buyer_info values (10000, 'user_10000', now(), now());
insert into buyer_info values (10001, 'user_10001', now(), now());
insert into buyer_info values (10999, 'user_10999', now(), now());
insert into buyer_balance values (10000, 101.00, 0, now(), now());
insert into buyer_balance values (10999, 100001.00, 0, now(), now());

-- 商户20000有余额记录，商户20001没有余额记录
insert into merchant_info values (20000, 'merchant_20000', now(), now());
insert into merchant_info values (20001, 'merchant_20001', now(), now());
insert into merchant_balance values (20000, 1000.00, 0, now(), now());

-- 商品30000有库存记录，商品30001没有库存记录
insert into sku_info values (30000, 'sku_30000', '199.99', 1, 20000, 'remark sku 30000', now(), now());
insert into sku_info values (30001, 'sku_30001', '300.18', 1, 20000, 'remark sku 30001', now(), now());
insert into sku_info values (30999, 'sku_30999', '301.19', 1, 20000, 'remark sku 30001', now(), now());
insert into sku_inventory values (30000, 99, 10099, now(), now());
insert into sku_inventory values (30999, 199, 10099, now(), now());