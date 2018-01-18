package com.incarcloud.rooster.datapack.model;

import org.junit.Test;

import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.GmmcCommandFacotry;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;
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
		ByteBuf buffer = facotry.createCommand(CommandType.OPEN_DOOR, "862234021042470", 0, "111111111", "v1.0",
				"testPackage", "www.incarcloud.com");
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.readBytes(bytes);
		System.out.println(HexStringUtil.toHexString(bytes));
	}

	@Test
	public void testLength() {
//		String s = "000000v1.0000000v1.0";
//		System.out.println(s.getBytes().length);

		String str = "2323010123FE38363232333430323130343234373001002D1201110B230C000476312E30000B746573745061636B61676500127777772E696E636172636C6F75642E636F6D00";

		byte[] checkBytes = HexStringUtil.parseBytes(str);

		checkBytes = GmmcDataPackUtils.addCheck(checkBytes);
		System.out.println(HexStringUtil.toHexString(checkBytes));
	}

}
