package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.strategy.impl.AlarmDataStrategy;
import com.incarcloud.rooster.datapack.strategy.impl.RunDataStrategy;
import org.junit.Ignore;
import org.junit.Test;

public class RunDataTest {

	private IDataPackStrategy strategy = new RunDataStrategy();

	private IDataPackStrategy strategy09 = new AlarmDataStrategy();

	@Test
	@Ignore
	public void test() throws Exception {
		String alarmStr = "23 23 02 01 09 FE 38 36 32 32 33 34 30 32 31 30 34 32 34 37 30 00 00 20 12 01 09 0B 20 27 06 06 77 E3 E1 02 08 91 6C 0B 40 00 00 32 00 01 20 00 00 01 01 01 01 01 01 01 25";
		ProtocolEngine engine = new ProtocolEngine();
		AlarmData alarm = engine.decode(HexStringUtil.parseBytes(alarmStr.replaceAll(" ", "")), AlarmData.class);

		// String bytes = "23 23 02 01 02 FE 38 36 32 32 33 34 30 32 31 30 34 32
		// 34 37 30 00 00 C5 12 01 08 12 17 37 01 01 01 27 10 4E 20 00 01 00 00
		// 1E 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88
		// 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13
		// 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 02 01 01 00 14 50 50 50
		// 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 03 01 01 01 02 58
		// 00 00 5B A0 03 E8 01 2C 50 01 0E EA 60 C8 00 14 DC DC DC DC 01 01 01
		// 01 01 01 01 01 01 01 0A 19 01 2C 01 00 00 04 01 01 02 50 03 E8 00 64
		// 5A 00 C8 00 1E 07 06 06 77 E1 A9 02 08 91 D3 00 00 00 00 17 D4 00 65
		// 00 00 00 00 08 01 03 13 88 01 04 13 88 01 02 50 01 03 50 A0";
		// System.out.println(bytes.replaceAll(" ", ""));
		// DataPack dataPack = new DataPack("test", "test", "test");
		// ByteBuf buf = Unpooled.buffer();
		// buf.writeBytes(HexStringUtil.parseBytes(bytes.replaceAll(" ", "")));
		// dataPack.setBuf(buf);
		// List<DataPackTarget> list = strategy09.decode(dataPack);
		// System.out.println(list);
		System.out.println(engine.toPrettyHexString(alarm));
	}

}
