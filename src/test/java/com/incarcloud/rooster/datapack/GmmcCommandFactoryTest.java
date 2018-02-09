package com.incarcloud.rooster.datapack;

import com.incarcloud.rooster.gather.cmd.CommandType;
import org.junit.Test;

/**
 * GmmcCommandFacotryTest
 *
 * @author Aaric, created on 2017-11-22T18:04.
 * @since 2.0
 */
public class GmmcCommandFactoryTest {

    /**
     * 参数设置
     *
     * @throws Exception
     */
    @Test
    public void testParamSet() throws Exception {
        GmmcCommandFactory factory = new GmmcCommandFactory();
        factory.createCommand(CommandType.SET_PARAMS, "862234021042470", 0, "111111111", 1, 1, 1, 1, 1, "2", 1, 1, 1, 1,
                1, 1, "3", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, "4", 1, 1, 1);
    }
}
