package com.yabobaozb.ecom;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = {
	"com.yabobaozb.ecom.buyer.infra.mapper",
	"com.yabobaozb.ecom.payment.infra.mapper",
	"com.yabobaozb.ecom.merchant.infra.mapper",
	"com.yabobaozb.ecom.commodity.infra.mapper",
	"com.yabobaozb.ecom.order.infra.mapper"
})
@SpringBootApplication
public class EcomApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcomApplication.class, args);
	}

}
