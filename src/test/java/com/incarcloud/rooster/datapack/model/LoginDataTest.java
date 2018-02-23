package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.gmmc.model.LoginData;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Base64;

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
	ProtocolEngine engine;
	String loginData;

	@Before
	public void init() {
		engine = new ProtocolEngine();
		loginData = "2323020101fe38363232333430323130343234373000" + "00b01201030a240a000100000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "00000000000000000000004c47574545554b3533484530303030333701010109";

		loginData = "2323020101fe3836323233343032313034323437300000b"
				+ "0120104092b23127700000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "0000000000000000000000000000000000000000000000000"
				+ "00000000000000000004c47574545554b3533484530303030" + "33370101014f";
	}

	@Test
	@Ignore
	public void test() throws Exception {

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

	@Test
	@Ignore
	public void testLoginData() throws Exception {
		LoginData data = engine.decode(HexStringUtil.parseBytes(loginData), LoginData.class);
		System.out.println(data);
		System.out.println(engine.toPrettyHexString(data));
	}

}
