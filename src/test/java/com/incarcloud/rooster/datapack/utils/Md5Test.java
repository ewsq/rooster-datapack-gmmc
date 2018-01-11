package com.incarcloud.rooster.datapack.utils;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @Title: Md5Test.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.utils
 * @Description: md5测试
 * @author: chenz
 * @date: 2018年1月11日 下午1:59:34
 * @version: V1.0
 */
public class Md5Test {
	
	@Test
	public void digestTest() {
		// bec98db9bd0c44dfa6e324e46cccb0f9
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		System.out.println(uuid);

		// 253ae5b5a5ec86773e491c65fb00f7b8
		String md5 = DigestUtils.md5Hex("bec98db9bd0c44dfa6e324e46cccb0f9").toUpperCase();
		System.out.println(md5);
	}
}
