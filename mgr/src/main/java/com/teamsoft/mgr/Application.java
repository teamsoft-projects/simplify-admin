package com.teamsoft.mgr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringBoot主入口
 */
@SpringBootApplication
@ComponentScan("com.teamsoft")
@MapperScan("com.teamsoft.*.*.mapper")
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
public class Application {
	/**
	 * 主入口
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}