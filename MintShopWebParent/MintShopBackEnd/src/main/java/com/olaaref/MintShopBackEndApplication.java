package com.olaaref;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.olaaref.mintshop.common.entity", "com.olaaref.mintshop"})
public class MintShopBackEndApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MintShopBackEndApplication.class, args);
	}

}
