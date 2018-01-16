package com.incarcloud.rooster.datapack.model;

import org.junit.Test;

import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.GmmcCommandFacotry;
import com.incarcloud.rooster.gather.cmd.CommandType;

import io.netty.buffer.ByteBuf;

/**
 * @Title: OtaUpdateDataTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: ota升级
 * @author: chenz
 * @date: 2018年1月15日 下午5:11:00
 * @version: V1.0
 */
public class OtaUpdateDataTest {
	@Test
	public void testOta() throws Exception {
		GmmcCommandFacotry facotry = new GmmcCommandFacotry();
		ByteBuf buffer = facotry.createCommand(CommandType.OTA, "862234021042470", "v1.0", "testPackage", "www.incarcloud.com");
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.readBytes(bytes);
		System.out.println(HexStringUtil.toHexString(bytes));
	}

	@Test
	public void testLength() {
		String s = "000000v1.0000000v1.0";
		System.out.println(s.getBytes().length);
	}
}
