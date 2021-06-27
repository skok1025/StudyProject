package com.skok1025.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.skok1025.study.library.libRedis;
import com.skok1025.study.library.libRedisTemplete;

import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/api/redis")
public class RedisController {
	
	@Autowired
	private libRedisTemplete libRedisTemplete;
	
	@Autowired
	private libRedis libRedis;

	@ResponseBody
	@GetMapping("/set1")
	public String set() {
		libRedisTemplete.set("hello_world", "value");
		return "set";
	}
	
	@ResponseBody
	@GetMapping("/set2")
	public String set2() {
		Jedis jedis = libRedis.getInstance("localhost", 6379).getConnection();
		return jedis.set("hello_world2", "value");
	}
}
