package com.incarcloud.rooster.datapack;

import org.junit.Ignore;
import org.junit.Test;

import com.incarcloud.rooster.gather.cmd.CommandType;

/**
 * GmmcCommandFacotryTest
 *
 * @author Aaric, created on 2017-11-22T18:04.
 * @since 2.0
 */
public class GmmcCommandFacotryTest {

	@Test
	@Ignore
	public void testPrint() {
		System.out.println("hello");
	}

	@Test
	public void testGreateCommand() {

	}

	/**
	 * 参数设置
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParamSet() throws Exception {
		GmmcCommandFacotry factory = new GmmcCommandFacotry();
		factory.createCommand(CommandType.SET_PARAMS, "862234021042470", 0, "111111111", 1, 1, 1, 1, 1, "2", 1, 1, 1, 1,
				1, 1, "3", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, "4", 1, 1, 1);
	}
}
