package com.teamsoft.mgr.common.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfigure {
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setUsername("");
		javaMailSender.setHost("");
		javaMailSender.setPort(25);
		javaMailSender.setPassword("");
		return javaMailSender;
	}
}