package com.incarcloud.rooster.datapack.model;

import java.util.Base64;

import org.junit.Ignore;
import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

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
	@Ignore
	public void test() throws Exception {
		ProtocolEngine engine = new ProtocolEngine();
		byte[] bytes = HexStringUtil.parseBytes("2323020101fe3836323233343032313034323437300000b0100b1c000f0d0001"
				+ "11110000000000000000000000000000000000000000000000000000001111"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000111111000000000000000000000000000000000000000000001111"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000111100000000000000000000000000000000000000000000000000000" + "0000000000000000000001010175");
		LoginData login = engine.decode(bytes, LoginData.class);

		int[] ints = login.getSecurityKey();

		// System.out.println(ints.length);
		// byte[] byteArray = new byte[128];
		// for (int i = 0; i < ints.length; i++) {
		// byteArray[i] = (byte) ints[i];
		// }
		// System.out.println(Base64.getEncoder().encodeToString(byteArray));
		byte[] re = Base64.getDecoder().decode(GmmcDataPackUtils.getBase64OfInt(ints));
		System.out.println(HexStringUtil.toHexString(re));
		System.out.println(login);
	}

}
