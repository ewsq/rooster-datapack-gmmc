package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @Title: ActivationDataTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: tbox激活/重置测试
 * @author: chenz
 * @date: 2018年1月9日 下午3:39:20
 * @version: V1.0
 */
public class ActivationDataTest {

	@Test
	@Ignore
	public void test() throws Exception {
		ProtocolEngine engie = new ProtocolEngine();
		String str = "2323020112fe38363232333430323130343234373000001c1201090f193500030101014c47574545554b35334845303030303337b9";// 激活
		String reset = "2323020113fe38363232333430323130343234373000001c1201090f193500030202024c47574545554b35334845303030303337bb";// 公钥重置
		ActivationData act = engie.decode(HexStringUtil.parseBytes(str), ActivationData.class);
		System.out.println(engie.toPrettyHexString(act));
	}

}
