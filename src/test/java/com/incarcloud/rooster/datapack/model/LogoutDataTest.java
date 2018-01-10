package com.incarcloud.rooster.datapack.model;

import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;

/**
 * @Title: LogoutDataTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 车辆登出报文解析测试
 * @author: chenz
 * @date: 2018年1月9日 下午4:36:21
 * @version: V1.0
 */
public class LogoutDataTest {

	@Test
	public void test() throws Exception {
		String out = "2323020105fe383632323334303231303432343730000008120109101e010001d8";
		ProtocolEngine engin = new ProtocolEngine();
		System.out.println(engin.toPrettyHexString(engin.decode(HexStringUtil.parseBytes(out), LogoutData.class)));
	}

}
