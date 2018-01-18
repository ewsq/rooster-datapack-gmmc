package com.incarcloud.rooster.datapack.model;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;

import junit.framework.Assert;

public class TestByteArray {
	@Number(width = 8)
	private int length;
	@Number(width = 8, length = "getLength", offset = 100)
	private byte[] values;

	@Test
	@Ignore
	public void test() throws Exception {
		ProtocolEngine engine = new ProtocolEngine();
		TestByteArray array = new TestByteArray();
		array.setValues(new byte[] { 1, 2, 3, 4, 5, 6 });
		array.setLength(6);
		byte[] buf = engine.encode(array);
		System.out.println(HexStringUtil.toHexString(buf));

		// Assert.assertTrue(buf[0] == (byte) 6);
		// for (int i = 1; i < 7; i++) {
		// Assert.assertTrue(buf[i] == (byte) (i + 100));
		// }
		//
		TestByteArray b = engine.decode(buf, TestByteArray.class);
		System.out.println(b.toString());
		Assert.assertTrue(b.getLength() == 6);
		Assert.assertTrue(Arrays.equals(b.getValues(), array.getValues()));
		System.out.println(engine.toPrettyHexString(b));
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getValues() {
		return values;
	}

	public void setValues(byte[] values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "TestByteArray{" + "length=" + length + ", values=" + Arrays.toString(values) + '}';
	}
}
