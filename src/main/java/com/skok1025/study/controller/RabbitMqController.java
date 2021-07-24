package com.skok1025.study.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.client.ConnectionFactory;
import com.skok1025.study.BootApp;
import com.skok1025.study.rabbitmq.RabbitMqManager;

@RestController
@RequestMapping("/api/rabbitmq")
public class RabbitMqController {
	
	@ResponseBody
	@GetMapping("/login/{userNo}")
	public String login(@PathVariable(value = "userNo") Long userNo) {
		// 로그인 시, user-inbox.{userNo} 로 큐를 생성
		BootApp.userMessageManager.onUserLogin(userNo);
		BootApp.userMessageManager.sendUserMessage(userNo, userNo + "::send");
		return userNo + "::send";
	}
	
	@ResponseBody
	@GetMapping("/consume/{userNo}")
	public String consume(@PathVariable(value = "userNo") Long userNo) {
		List<String> messages = BootApp.userMessageManager.fetchUserMessages(userNo);
		
		System.out.println(messages);
		return messages.toString();
	}

}
