package com.incarcloud.rooster.datapack.model;

import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

/**
 * @Title: GmmcPackageTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 整包数据解析测试
 * @author: chenz
 * @date: 2018年1月18日 下午3:45:44
 * @version: V1.0
 */
public class GmmcPackageTest {
	@Test
	public void testParse() throws Exception {
		String s = "2323020102fe38363232333430323130343234373000" + "00c51201120f2e1e01010127104e20000100001e13881"
				+ "388138813881388138813881388138813881388138813" + "881388138813881388138813881388138813881388138"
				+ "813881388138813881388138802010100145050505050" + "505050505050505050505050505050030101010258000"
				+ "05ba003e8012c50010eea60c80014dcdcdcdc01010101" + "0101010101010a19012c010000040101025003e800645"
				+ "a00c8001e070600000000000000000000000000000000" + "0000000008010313880104138801025001035060";
		ProtocolEngine engine = new ProtocolEngine();
		GmmcPackage gmmc = engine.decode(HexStringUtil.parseBytes(s), GmmcPackage.class);
		int[] ints = gmmc.getBodyBuffer();
		byte[] bytes = GmmcDataPackUtils.coverToByteArray(ints);
		
		
	
		System.out.println(bytes);
	}
}
