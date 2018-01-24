package com.incarcloud.rooster.datapack.model;

import org.junit.Ignore;
import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

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
	@Ignore
	public void test() throws Exception {
		String in = "2323020102fe3836323233343032313034323437300000c5100b1c000f170101012"
				+ "7104e20000100001e1388138813881388138813881388138813881388138813881388"
				+ "138813881388138813881388138813881388138813881388138813881388138813880"
				+ "201010014505050505050505050505050505050505050505003010101025800005ba0"
				+ "03e8012c50010eea60c80014dcdcdcdc010101010101010101010a19012c010000040"
				+ "101025003e800645a00c8001e07060000000000000000000000000000000000000000"
				+ "08010313880104138801025001035041";
		
		
		ProtocolEngine engin = new ProtocolEngine();

		
		LoginData login = engin.decode(HexStringUtil.parseBytes(in), LoginData.class);
		login.getHeader().setImei("962234021042470");
		login.getTail().setCheck(0);
		byte[] bytes = GmmcDataPackUtils.addCheck(engin.encode(login));
		System.out.println(HexStringUtil.toHexString(bytes));
	}

}
