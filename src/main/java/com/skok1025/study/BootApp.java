package com.skok1025.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rabbitmq.client.ConnectionFactory;
import com.skok1025.study.rabbitmq.RabbitMqManager;
import com.skok1025.study.rabbitmq.UserMessageManager;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class}) // no database setting
public class BootApp {
	
	public static UserMessageManager userMessageManager;
	
	public static void main(String[] args) {
		SpringApplication.run(BootApp.class,args);
		rabbitMqUserMessageManagerStart();
	}
	
	private static void rabbitMqUserMessageManagerStart() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setHost("localhost");
		factory.setPort(5672);
		
		RabbitMqManager connectionManager = new RabbitMqManager(factory);
		connectionManager.start();
		
		userMessageManager = new UserMessageManager(connectionManager);
		userMessageManager.onApplicationStart();
	}

}