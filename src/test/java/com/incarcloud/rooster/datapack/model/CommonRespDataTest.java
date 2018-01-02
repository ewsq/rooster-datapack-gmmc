package com.incarcloud.rooster.datapack.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

/**
 * @Title: CommonRespDataTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 通用应答测试类
 * @author: chenz
 * @date: 2018年1月2日 上午10:39:08
 * @version: V1.0
 */
public class CommonRespDataTest {

	@Test
	public void test() throws Exception {
		ProtocolEngine engin = new ProtocolEngine();
		String resp = "2323020101013836323233343032313034323437300000061201020A163403";
		CommonRespData data = engin.decode(HexStringUtil.parseBytes(resp), CommonRespData.class);

		Date date = new Date(data.getGatherTime());
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

		System.out.println(localDateTime);
	}

	@Test
	public void checkTest() {
		String resp = "2323020101013836323233343032313034323437300000010c00";
		byte[] bytes = GmmcDataPackUtils.addCheck(HexStringUtil.parseBytes(resp));
		System.out.println(HexStringUtil.toHexString(bytes));
	}
}
