package com.incarcloud.rooster.datapack.model;

import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.GmmcCommandFacotry;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.gather.cmd.CommandType;

import io.netty.buffer.ByteBuf;

/**
 * @Title: DownlinkControlAirDataTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 下行控制指令测试-多参数
 * @author: chenz
 * @date: 2018年1月16日 下午4:43:19
 * @version: V1.0
 */
public class DownlinkControlAirDataTest {
	@Test
	public void testDownlink() throws Exception {
		ProtocolEngine engine = new ProtocolEngine();
		DownlinkControlAirData downlink = new DownlinkControlAirData();
		// 报文头部信息
		Header header = new Header();
		Tail tail = new Tail();
		// 下行控制默认报文体
		header.setCarFlag(0x01);// 车辆类型标识 0x01燃油车 ；0x02 新能源车
		header.setCarType(0x01);// 车型 0x01 ZC；0xRE NS；0x03 NS
		header.setCmdFlag(0x83);// 命令标识-车辆控制命令
		header.setResponeFlag(0xFE);// 应答标识 命令包
		header.setImei("862234021042470");// imei
		header.setEncryptType(0x01);// 加密方式
		header.setLength(0x00);// 数据单元长度
		downlink.setHeader(header);// 设置头部信息
		downlink.setTail(tail);// 设置尾部信息

		downlink.setGatherTime(System.currentTimeMillis());// 报文时间
		downlink.setSerialNumber(0);// 流水号
		downlink.setCmdNumber(1);// 指令数量-目前只支持单条指令下发
		downlink.setCmdId(0x06);
		downlink.setCmdMode(1);
		downlink.setCmdLength(4);
		downlink.setTimeSetting(100);
		downlink.setTemSetting(20);

		byte[] bytes = engine.encode(downlink);
		bytes = GmmcDataPackUtils.addCheck(bytes);
		DownlinkControlAirData data = engine.decode(bytes, DownlinkControlAirData.class);
		System.out.println(data);
		System.out.println(HexStringUtil.toHexString(bytes));

	}

	@Test
	public void testAirControll() throws Exception {
		GmmcCommandFacotry facotry = new GmmcCommandFacotry();
		ByteBuf buffer = facotry.createCommand(CommandType.COND_HEAT_CLOSE, "862234021042470",0,"111111111", 10, 20);
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.readBytes(bytes);
		System.out.println(HexStringUtil.toHexString(bytes));
	}
}
