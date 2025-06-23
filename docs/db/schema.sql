-- 创建数据库
create database ecom;

-- 创建用户
CREATE USER 'ecom_wr'@'%' IDENTIFIED BY '1q2w3e4r5t!Q';
GRANT ALL PRIVILEGES ON ecom.* TO 'ecom_wr'@'%';
FLUSH PRIVILEGES;

-- 出现问题后修复，问题：Public Key Retrieval is not allowed
-- ALTER USER 'ecom_wr'@'%' IDENTIFIED WITH mysql_native_password BY '1q2w3e4r5t!Q';
-- FLUSH PRIVILEGES;

-- 创建数据表
use ecom;

-- 来自mysqldump

-- MySQL dump 10.13  Distrib 8.0.42, for macos13.7 (x86_64)
--
-- Host: localhost    Database: ecom
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `buyer_balance`
--

DROP TABLE IF EXISTS `buyer_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buyer_balance` (
                                 `buyer_id` bigint unsigned NOT NULL,
                                 `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额，单位是元',
                                 `version` bigint NOT NULL DEFAULT '0' COMMENT '版本',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`buyer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户余额表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `buyer_balance_record`
--

DROP TABLE IF EXISTS `buyer_balance_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buyer_balance_record` (
                                        `record_id` bigint unsigned NOT NULL,
                                        `buyer_id` bigint unsigned NOT NULL,
                                        `change_type` smallint NOT NULL COMMENT '变动类型',
                                        `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '变动金额，单位是元',
                                        `balance_from` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '变动前金额，单位是元',
                                        `balance_to` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '变动后金额，单位是元',
                                        `pay_id` bigint NOT NULL DEFAULT '0' COMMENT '支付ID',
                                        `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                                        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`record_id`),
                                        KEY `idx_buyer_pay` (`buyer_id`,`pay_id`),
                                        KEY `idx_create_time_type` (`create_time`,`change_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户余额变动记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `buyer_info`
--

DROP TABLE IF EXISTS `buyer_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buyer_info` (
                              `buyer_id` bigint unsigned NOT NULL,
                              `name` varchar(50) NOT NULL COMMENT '用户名称',
                              `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`buyer_id`),
                              UNIQUE KEY `uni_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `merchant_balance`
--

DROP TABLE IF EXISTS `merchant_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_balance` (
                                    `merchant_id` bigint unsigned NOT NULL,
                                    `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额，单位是元',
                                    `version` bigint NOT NULL DEFAULT '0' COMMENT '版本',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户余额表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `merchant_balance_record`
--

DROP TABLE IF EXISTS `merchant_balance_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_balance_record` (
                                           `record_id` bigint unsigned NOT NULL,
                                           `merchant_id` bigint unsigned NOT NULL,
                                           `change_type` smallint NOT NULL COMMENT '变动类型',
                                           `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '变动金额，单位是元',
                                           `balance_from` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '变动前金额，单位是元',
                                           `balance_to` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '变动后金额，单位是元',
                                           `pay_id` bigint NOT NULL DEFAULT '0' COMMENT '支付ID',
                                           `source_order_id` bigint NOT NULL DEFAULT '0' COMMENT '订单ID',
                                           `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           PRIMARY KEY (`record_id`),
                                           KEY `idx_merchant_pay` (`merchant_id`,`pay_id`),
                                           KEY `idx_create_time_type` (`create_time`,`change_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户余额变动记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `merchant_info`
--

DROP TABLE IF EXISTS `merchant_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_info` (
                                 `merchant_id` bigint unsigned NOT NULL,
                                 `name` varchar(50) NOT NULL COMMENT '名称',
                                 `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`merchant_id`),
                                 UNIQUE KEY `uni_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_info`
--

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_info` (
                              `order_id` bigint unsigned NOT NULL,
                              `amount_payable` decimal(10,2) NOT NULL COMMENT '应付',
                              `amount_paid` decimal(10,2) NOT NULL COMMENT '实付',
                              `sku_size` int unsigned NOT NULL COMMENT 'SKU种类数量',
                              `total_quantity` int unsigned NOT NULL COMMENT 'SKU总数量',
                              `pay_result` smallint unsigned NOT NULL COMMENT '支付结果',
                              `order_status` smallint unsigned NOT NULL COMMENT '订单状态',
                              `buyer_id` bigint unsigned NOT NULL COMMENT '购买人',
                              `merchant_id` bigint unsigned NOT NULL,
                              `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
                              `item_id` bigint unsigned NOT NULL,
                              `order_id` bigint unsigned NOT NULL,
                              `sku_id` bigint unsigned NOT NULL,
                              `sku_name` varchar(50) NOT NULL COMMENT '名称',
                              `quantity` int NOT NULL DEFAULT '0' COMMENT '数量',
                              `unit_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '销售单价，单位是元',
                              `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`item_id`),
                              KEY `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_payment`
--

DROP TABLE IF EXISTS `order_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_payment` (
                                 `pay_id` bigint unsigned NOT NULL,
                                 `order_id` bigint unsigned NOT NULL,
                                 `amount` decimal(10,2) NOT NULL COMMENT '应付',
                                 `pay_type` smallint NOT NULL COMMENT '支付类型，1现金，2余额',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`pay_id`),
                                 KEY `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单支付表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment_info`
--

DROP TABLE IF EXISTS `payment_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_info` (
                                `pay_id` bigint unsigned NOT NULL COMMENT '支付ID',
                                `amount` decimal(10,2) NOT NULL COMMENT '支付金额',
                                `pay_type` smallint NOT NULL COMMENT '支付类型，1现金，2余额',
                                `source_order_id` bigint NOT NULL COMMENT '原始交易ID',
                                `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`pay_id`),
                                KEY `idx_source_id` (`source_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settlement_info`
--

DROP TABLE IF EXISTS `settlement_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settlement_info` (
                                   `settle_id` bigint unsigned NOT NULL,
                                   `merchant_id` bigint unsigned NOT NULL,
                                   `settle_time` char(10) NOT NULL COMMENT '结算时间',
                                   `begin_at` char(19) NOT NULL COMMENT '开始时间（含）',
                                   `end_at` char(19) NOT NULL COMMENT '结束时间（不含）',
                                   `expect_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '预期结算金额，单位是元',
                                   `settle_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实际结算金额，单位是元',
                                   `diff_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '差异金额，单位是元',
                                   `settle_result` smallint NOT NULL,
                                   `remark` varchar(200) NOT NULL COMMENT '备注',
                                   `version` bigint unsigned NOT NULL COMMENT '版本',
                                   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`settle_id`),
                                   UNIQUE KEY `uni_merchant_settle_time` (`merchant_id`,`settle_time`),
                                   KEY `idx_merchant_time` (`merchant_id`,`settle_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='结算信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `settlement_info_record`
--

DROP TABLE IF EXISTS `settlement_info_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settlement_info_record` (
                                          `record_id` bigint unsigned NOT NULL,
                                          `settle_id` bigint unsigned NOT NULL,
                                          `merchant_id` bigint unsigned NOT NULL,
                                          `settle_time` char(10) NOT NULL COMMENT '结算时间',
                                          `begin_at` char(19) NOT NULL COMMENT '开始时间（含）',
                                          `end_at` char(19) NOT NULL COMMENT '结束时间（不含）',
                                          `expect_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '预期结算金额，单位是元',
                                          `settle_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实际结算金额，单位是元',
                                          `diff_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '差异金额，单位是元',
                                          `settle_result` smallint NOT NULL,
                                          `remark` varchar(200) NOT NULL COMMENT '备注',
                                          `version` bigint unsigned NOT NULL COMMENT '版本',
                                          `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                          PRIMARY KEY (`record_id`),
                                          KEY `idx_settle_id` (`settle_id`),
                                          KEY `idx_merchant_time` (`merchant_id`,`settle_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='结算版本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sku_info`
--

DROP TABLE IF EXISTS `sku_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sku_info` (
                            `sku_id` bigint unsigned NOT NULL,
                            `name` varchar(50) NOT NULL COMMENT '名称',
                            `sale_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '销售价格，单位是元',
                            `sale_status` smallint NOT NULL DEFAULT '1' COMMENT '销售状态，0否1是',
                            `merchant_id` bigint unsigned NOT NULL,
                            `remark` varchar(200) DEFAULT NULL COMMENT '描述',
                            `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`sku_id`),
                            UNIQUE KEY `uni_name` (`name`),
                            KEY `idx_merchant` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品SKU信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sku_inventory`
--

DROP TABLE IF EXISTS `sku_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sku_inventory` (
                                 `sku_id` bigint unsigned NOT NULL,
                                 `available_inventory` int NOT NULL DEFAULT '0' COMMENT '可用库存，单位是件',
                                 `version` bigint NOT NULL DEFAULT '0' COMMENT '版本',
                                 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品SKU库存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sku_inventory_record`
--

DROP TABLE IF EXISTS `sku_inventory_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sku_inventory_record` (
                                        `record_id` bigint unsigned NOT NULL,
                                        `sku_id` bigint unsigned NOT NULL,
                                        `change_type` smallint NOT NULL COMMENT '变动类型',
                                        `quantity` int NOT NULL DEFAULT '0' COMMENT '变动数量',
                                        `inventory_from` int NOT NULL DEFAULT '0' COMMENT '变动前数量',
                                        `inventory_to` int NOT NULL DEFAULT '0' COMMENT '变动后数量',
                                        `pay_id` bigint NOT NULL DEFAULT '0' COMMENT '支付ID',
                                        `soruce_order_id` bigint NOT NULL DEFAULT '0' COMMENT '订单ID',
                                        `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                                        `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`record_id`),
                                        KEY `idx_sku_pay` (`sku_id`,`pay_id`),
                                        KEY `idx_create_time_type` (`create_time`,`change_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品SKU库存变动记录';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-23 13:35:51
