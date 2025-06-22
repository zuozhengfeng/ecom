package com.yabobaozb.ecom;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(value = {
	"com.yabobaozb.ecom.buyer.infra.mapper",
	"com.yabobaozb.ecom.payment.infra.mapper",
	"com.yabobaozb.ecom.merchant.infra.mapper",
	"com.yabobaozb.ecom.commodity.infra.mapper",
	"com.yabobaozb.ecom.order.infra.mapper",
	"com.yabobaozb.ecom.settlement.infra.mapper"
})
@EnableScheduling
@EnableTransactionManagement
//@EnableAspectJAutoProxy
@SpringBootApplication
public class EcomApplication {

	private final static Logger logger = LoggerFactory.getLogger(EcomApplication.class);
	public static void main(String[] args) {
		logger.info("Application starting ...");
		SpringApplication.run(EcomApplication.class, args);
		logger.info("Application started");
	}

}
