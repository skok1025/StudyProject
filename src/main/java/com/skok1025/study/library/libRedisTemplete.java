package com.skok1025.study.library;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class libRedisTemplete {
	@Autowired
    RedisTemplate<String, Object> redisTemplate;
	
	public void set(String key, String val) {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(key, val);
	}
	public void setex(String key, int expire, String val) {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        vop.set(key, val, expire);
	}
	
	public String get(String key) {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		
		return (String) vop.get(key);
	}
	
	public Boolean del(String key) {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		return ((RedisTemplate<String, Object>) vop).delete(key);
	}
	
	public void zadd(String setName, int score, String value) {
		ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
		zSetOperations.add(setName, value, score);
	}
	
	public Set<Object> zrange(String setName, int start, int end) {
		ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
		return zSetOperations.range(setName, start, end);
	}
	
	public Set<Object> zrevrange(String setName, int start, int end) {
		ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
		return zSetOperations.reverseRange(setName, start, end);
	}
	
	public Long zrem(String setName, String value) {
		ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
		return zSetOperations.remove(setName, value);
	}
	
	public Double getRecentScore(String setName) {
		ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
		
		for (Object member : zSetOperations.range(setName, -1, -1)) {
			return getScore(setName, (String)member);
		}
		return null;
	}
	
	public Double getScore(String setName, String member) {
		ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
		return zSetOperations.score(setName, member);
	}
	
	public Long incr(String key) {
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		return vop.increment(key);
	}
	
	public Long hincrBy(String key, String field, int incr) {
		HashOperations<String, String, Integer> hashOp = redisTemplate.opsForHash();
		return hashOp.increment(key, field, incr);
	}
	
	public String hget(String key, String field) {
		HashOperations<String, String, Integer> hashOp = redisTemplate.opsForHash();
		
		return String.valueOf(hashOp.get(key, field));
	}
	
	public Set<String> hkeys(String key) {
		HashOperations<String, String, Integer> hashOp = redisTemplate.opsForHash();
		
		return hashOp.keys(key);
	}
	
	
}
