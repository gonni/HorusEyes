package com.yg.horus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EnableTransactionManagement
@EnableWebMvc
//@ComponentScan("com.yg.horus")
//@EntityScan(basePackages = "com.yg.horus.data")
public class HorusCrawlMain {
	public static void main(String[] args) {
		SpringApplication.run(HorusCrawlMain.class, args);
	}
}
