package com.incarcloud.rooster.datapack;

import org.junit.Ignore;
import org.junit.Test;

import com.github.io.protocol.utils.HexStringUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * DataParserGmmcTest
 *
 * @author Aaric, created on 2017-11-22T18:02.
 * @since 2.0
 */
public class DataParserGmmcTest {

	@Test
	@Ignore
	public void testPrint() {
		System.out.println("hello");
	}

	/**
	 * 应答消息
	 */
	@Test
	public void testCreateResponse() {
		DataParserGmmc parse = new DataParserGmmc();

		DataPack dataPack = new DataPack("test", "test", "test");

		ByteBuf buf = Unpooled.buffer();
		buf.writeBytes(HexStringUtil.parseBytes("2323020101fe3836323233343032313034323437300000b0100b1c000f0d0001"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000000000000000000000000000000000000000000000000000000000"
				+ "00000000000000000000000000000000000000000000000000000000000000" + "0000000000000000000001010175"));
		dataPack.setBuf(buf);
		ByteBuf resp = parse.createResponse(dataPack, ERespReason.OK);
		byte[] bytes = resp.array();
		System.out.println(HexStringUtil.toHexString(bytes));
	}
}
