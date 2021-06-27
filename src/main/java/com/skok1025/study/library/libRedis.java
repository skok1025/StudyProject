package com.skok1025.study.library;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class libRedis {
	private static String redisHost;
	private static int redisPort;

	private final Set<Jedis> connectionList = new HashSet<Jedis>();
	private JedisPool pool;
	
	private libRedis() {
		this.pool = new JedisPool(redisHost, redisPort);
	}
	
	private static class LazyHolder {
		private static final libRedis INSTANCE = new libRedis();
	}
	
	public static libRedis getInstance() {
		libRedis.redisHost = "localhost";
		libRedis.redisPort = 6379;
		return LazyHolder.INSTANCE;
	}
	
	public static libRedis getInstance(String redisHost, int redisPort) {
		libRedis.redisHost = redisHost;
		libRedis.redisPort = redisPort;
		return LazyHolder.INSTANCE;
	}
	
	final public Jedis getConnection() {
		Jedis jedis = this.pool.getResource();
		this.connectionList.add(jedis);
		
		return jedis;
	}
	
	final public void returnResource(Jedis jedis) { 
		this.pool.returnBrokenResource(jedis);
	}

	final public void destroyPool() {
		Iterator<Jedis> jedisList = this.connectionList.iterator();
		while (jedisList.hasNext()) {
			Jedis jedis = jedisList.next();
			this.pool.returnResource(jedis);
		}
		
		this.pool.destroy();
	}
}
