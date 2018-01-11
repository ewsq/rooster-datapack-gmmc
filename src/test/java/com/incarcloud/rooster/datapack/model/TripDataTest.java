package com.incarcloud.rooster.datapack.model;

import org.junit.Test;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;

public class TripDataTest {

	@Test
	public void test() throws Exception {
		String str = "232302010bfe38363232333430323130343234373000001b100b1c000000100b1c01000001f407d00834003c00002710050505c0";
		ProtocolEngine engie = new ProtocolEngine();
		TripData trip = engie.decode(HexStringUtil.parseBytes(str), TripData.class);
		System.out.println(engie.toPrettyHexString(trip));
	}

}
