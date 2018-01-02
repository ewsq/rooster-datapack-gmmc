package com.incarcloud.rooster.datapack.model;

import org.junit.Ignore;
import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;

/**
 * @Title: LoginDataTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 登录报文
 * @author: chenz
 * @date: 2018年1月2日 下午2:55:05
 * @version: V1.0
 */
public class LoginDataTest {

	@Test
	public void test() throws Exception {
		ProtocolEngine engine = new ProtocolEngine();
		byte[] bytes = HexStringUtil.parseBytes("2323020101fe3836323233343032313034323437300000b0100b1c000f0d0001"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000000000000000000000000000000000000000000000000000000000" + "0000000000000000000001010175");
		LoginData login = engine.decode(bytes, LoginData.class);
		System.out.println(login);
	}

}
