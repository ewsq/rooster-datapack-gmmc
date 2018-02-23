package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.gmmc.model.CommonRespData;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

	String resp;

	@Before
	public void before() {
		ProtocolEngine engin = new ProtocolEngine();
		resp = "2323020101013836323233343032313034323437300000061201020A163403";
	}

	@Test
	@Ignore
	public void test() throws Exception {
		ProtocolEngine engin = new ProtocolEngine();
		String resp = "2323020102fe3836323233343032313034323437300000061201040b0e06d2";
		CommonRespData data = engin.decode(HexStringUtil.parseBytes(resp), CommonRespData.class);

		Date date = new Date(data.getGatherTime());
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		System.out.println(data);
		System.out.println(localDateTime);
	}

	@Test
	@Ignore
	public void checkTest() {
		String resp = "2323020101013836323233343032313034323437300000010c00";
		byte[] bytes = GmmcDataPackUtils.addCheck(HexStringUtil.parseBytes(resp));
		System.out.println(HexStringUtil.toHexString(bytes));
	}

	@Test
	@Ignore
	public void bufferTest() throws UnsupportedEncodingException {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeBytes(HexStringUtil.parseBytes(resp));

	}
}
