package com.incarcloud.rooster.datapack.model;

import org.junit.Ignore;
import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;

public class ParameterSettingDataTest {

	@Test
	@Ignore
	public void test() throws Exception {
		String s = "2323010121fe383632323334303231303432343" + "7300100581201120a0d300200010300010400010"
				+ "5000106000107320800010900010a00010b00010" + "c010d00010e330f0001100182000183018400018"
				+ "500018600018700018800018900018a00018b000" + "18c00018d348e00018f01990132";

		ParameterSettingData setting = new ParameterSettingData();
		ProtocolEngine engine = new ProtocolEngine();
		setting = engine.decode(HexStringUtil.parseBytes(s), ParameterSettingData.class);
		System.out.println(engine.toPrettyHexString(setting));
	}

}
