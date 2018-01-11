package com.incarcloud.rooster.datapack.config;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @Title: RedisConfigTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.config
 * @Description: redis
 * @author: chenz
 * @date: 2018年1月10日 上午11:45:20
 * @version: V1.0
 */
public class RedisConfigTest {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void test() {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		Set<String> names = redisTemplate.keys("com.incarcloud.rooster:vehicle-vin:*");
	}

}
