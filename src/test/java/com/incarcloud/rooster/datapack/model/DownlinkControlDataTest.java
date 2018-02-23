package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.GmmcCommandFactory;
import com.incarcloud.rooster.datapack.gmmc.model.DownlinkControlData;
import com.incarcloud.rooster.datapack.gmmc.model.Header;
import com.incarcloud.rooster.datapack.gmmc.model.Tail;
import com.incarcloud.rooster.gather.cmd.CommandType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Base64;

/**
 * @Title: DownlinkControlDataTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 下行控制指令测试
 * @author: chenz
 * @date: 2018年1月16日 上午10:55:55
 * @version: V1.0
 */
public class DownlinkControlDataTest {
	GmmcCommandFactory factory;

	@Before
	public void init() {
		factory = new GmmcCommandFactory();
	}

	@Test
	@Ignore
	public void testOpenDoor() throws Exception {
		ByteBuf buf = factory.createCommand(CommandType.OPEN_DOOR, "862234021042470", 0, "111111111");
		byte[] bytes = ByteBufUtil.getBytes(buf);
		System.out.println(Base64.getEncoder().encodeToString(bytes));
	}

	@Test
	@Ignore
	public void testByteArray() throws Exception {
		ProtocolEngine engine = new ProtocolEngine();
		DownlinkControlData downlink = new DownlinkControlData();
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
		downlink.setCmdContent(1);
		downlink.setCmdLength(6);
		byte[] bytes = engine.encode(downlink);

		DownlinkControlData data = engine.decode(bytes, DownlinkControlData.class);
		System.out.println(data);
		System.out.println(HexStringUtil.toHexString(bytes));
	}
}
