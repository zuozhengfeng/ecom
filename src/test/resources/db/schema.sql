CREATE TABLE IF NOT EXISTS buyer_info (
      buyer_id BIGINT NOT NULL,
      name varchar(50) NOT NULL,
      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (buyer_id)
);

CREATE TABLE IF NOT EXISTS buyer_balance (
       buyer_id BIGINT NOT NULL,
       balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
       version BIGINT NOT NULL DEFAULT 0,
       create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       PRIMARY KEY (buyer_id)
);

CREATE TABLE IF NOT EXISTS buyer_balance_record (
        record_id BIGINT PRIMARY KEY ,
        buyer_id BIGINT NOT NULL,
        amount DECIMAL(10,2) NOT NULL,
    change_type SMALLINT NOT NULL,
    balance_from DECIMAL(10,2) NOT NULL,
    balance_to DECIMAL(10,2) NOT NULL,
    pay_id BIGINT NOT NULL,
    remark VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (record_id)
);

CREATE TABLE IF NOT EXISTS payment_info (
    pay_id BIGINT PRIMARY KEY ,
    amount DECIMAL(10,2) NOT NULL,
    pay_type SMALLINT NOT NULL,
    source_order_id BIGINT NOT NULL,
    remark VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (pay_id)
);

CREATE TABLE IF NOT EXISTS  merchant_info (
                                 merchant_id bigint NOT NULL,
                                 name varchar(50) NOT NULL,
                                 create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                 update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (merchant_id)
);


CREATE TABLE IF NOT EXISTS  merchant_balance (
                                    merchant_id bigint NOT NULL,
                                    balance decimal(10,2) NOT NULL DEFAULT '0.00',
                                    version bigint NOT NULL DEFAULT 0,
                                    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (merchant_id)
);

CREATE TABLE IF NOT EXISTS  merchant_balance_record (
                                           record_id bigint NOT NULL,
                                           merchant_id bigint NOT NULL,
                                           change_type smallint NOT NULL,
                                           amount decimal(10,2) NOT NULL DEFAULT '0.00',
                                           balance_from decimal(10,2) NOT NULL DEFAULT '0.00',
                                           balance_to decimal(10,2) NOT NULL DEFAULT '0.00',
                                           pay_id bigint NOT NULL DEFAULT 0,
                                           source_order_id bigint NOT NULL DEFAULT 0,
                                           remark varchar(200) DEFAULT NULL,
                                           create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           PRIMARY KEY (record_id)
);

CREATE TABLE IF NOT EXISTS  sku_info (
                            sku_id bigint NOT NULL,
                            name varchar(50) NOT NULL,
                            sale_price decimal(10,2) NOT NULL DEFAULT '0.00',
                            sale_status smallint NOT NULL DEFAULT 1,
                            merchant_id bigint NOT NULL,
                            remark varchar(200) DEFAULT NULL,
                            create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (sku_id)
);

CREATE TABLE IF NOT EXISTS  sku_inventory (
                                 sku_id bigint NOT NULL,
                                 available_inventory int NOT NULL DEFAULT 0,
                                 version bigint NOT NULL DEFAULT 0,
                                 create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (sku_id)
);

CREATE TABLE IF NOT EXISTS  sku_inventory_record (
                                        record_id bigint NOT NULL,
                                        sku_id bigint NOT NULL,
                                        change_type smallint NOT NULL,
                                        quantity int NOT NULL DEFAULT 0,
                                        inventory_from int NOT NULL DEFAULT 0,
                                        inventory_to int NOT NULL DEFAULT 0,
                                        pay_id bigint NOT NULL DEFAULT 0,
                                        soruce_order_id bigint NOT NULL DEFAULT 0,
                                        remark varchar(200) DEFAULT NULL,
                                        create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        PRIMARY KEY (record_id)
);


CREATE TABLE IF NOT EXISTS  order_info (
                              order_id bigint NOT NULL,
                              amount_payable decimal(10,2) NOT NULL ,
                              amount_paid decimal(10,2) NOT NULL ,
                              sku_size int NOT NULL ,
                              total_quantity int NOT NULL ,
                              pay_result smallint NOT NULL ,
                              order_status smallint NOT NULL ,
                              merchant_id bigint NOT NULL,
                              buyer_id bigint NOT NULL ,
                              remark varchar(200) DEFAULT NULL ,
                              create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                              update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                              PRIMARY KEY (order_id)
);

CREATE TABLE IF NOT EXISTS  order_item (
                              item_id bigint NOT NULL,
                              order_id bigint NOT NULL,
                              sku_id bigint NOT NULL,
                              sku_name varchar(50) NOT NULL ,
                              quantity int NOT NULL DEFAULT '0' ,
                              unit_price decimal(10,2) NOT NULL DEFAULT '0.00' ,
                              create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP ,
                              update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP ,
                              PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS  order_payment (
                                 pay_id bigint NOT NULL,
                                 order_id bigint NOT NULL,
                                 amount decimal(10,2) NOT NULL ,
                                 pay_type smallint NOT NULL ,
                                 create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                                 update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
                                 PRIMARY KEY (pay_id)
);



