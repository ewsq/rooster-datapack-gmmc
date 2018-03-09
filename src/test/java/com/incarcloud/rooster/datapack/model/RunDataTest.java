package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.DataParserGmmc;
import com.incarcloud.rooster.datapack.GmmcCommandFactory;
import com.incarcloud.rooster.datapack.gmmc.model.AlarmData;
import com.incarcloud.rooster.datapack.gmmc.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.gmmc.strategy.impl.AlarmDataStrategy;
import com.incarcloud.rooster.datapack.gmmc.strategy.impl.RunDataStrategy;
import com.incarcloud.rooster.gather.cmd.CommandType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Base64;
import java.util.List;

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

	@Test
	@Ignore
	public void runData() throws Exception {
		GmmcCommandFactory facotry = new GmmcCommandFactory();
		Object[] args = new Object[6] ;
		args[0] = "862234021042470" ;
		args[1] = 1 ;
		args[2] = "MTljMDIwMjAtYmVjYi00AA==" ;
		ByteBuf buffer = facotry.createCommand(CommandType.GET_RUN_INFO,args) ;
		byte[] bytes = ByteBufUtil.getBytes(buffer) ;

		System.out.println(HexStringUtil.toHexString(bytes));
	}

	@Test
	@Ignore
	public void runDataTest(){
		 String bytes = "2323010102FE39313131313131313131313131313902005046C25C932674905521DA6119EFAFF7094E59A4E397417E2D9C5001C99CFE279A30D79057601CE8FBBACCF8AB5364F937900B989BFCE159C0DC970E871669C695DB74AC6CE0F812E596E041F995277B8513";
		 DataPack dataPack = new DataPack("test", "test", "test");
		 ByteBuf buf = Unpooled.buffer();
		 buf.writeBytes(HexStringUtil.parseBytes(bytes.replaceAll(" ", "")));
		 dataPack.setBuf(buf);
		DataParserGmmc dataParserGmmc = new DataParserGmmc() ;

		String deviceId = "911111111111119" ;
//		String privateKeye = "AKK4Nd+UzZlKinaojsM8NVTpNbn2ghTAcuWhtdWIaBXyLky1c1zMA44s8IggsWVcwrrX0+uOMSk6Ipri0evoFsgUGrRh9korYqZTEHpeZs1o0vMZRZDmcmm8hrz3HP/ADyYAQBcRg/71Y2NUMTNLxgkjNXgggrE6O7xXJUc5QigB";
//		String privateKeyn = "AKM0hmWjszvrjEF8peF/23Vth6P2fNXcmNHjqf4QxlSmalt9FNwOIxDDMqFvkBE7NvdB7rpLScVS1JoiOr8+8GPx4VPdqHcqdubKCPBWWe1CPmlSlfKQHZACEfZ6qT0Eq8CP4g+s4jSRw3u0z8U8knL3v3YVG1ScijS9HEei3LIx" ;
//		byte[] n = Base64.getDecoder().decode(privateKeyn) ;
//		byte[] e = Base64.getDecoder().decode(privateKeye) ;
//		dataParserGmmc.setPrivateKey(deviceId, n,e);
		dataParserGmmc.setSecurityKey(deviceId,Base64.getDecoder().decode("71377NFhCqUMfrQ9CapOYA=="));

		List<DataPack> dataPacks = dataParserGmmc.extract(buf) ;
		 List<DataPackTarget> list = strategy.decode(dataPacks.get(0));
		 System.out.println(list);
	}

}
