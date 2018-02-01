package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.GmmcCommandFacotry;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.gather.cmd.CommandType;
import io.netty.buffer.ByteBuf;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @Title: OtaUpdateDataTest.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: ota升级
 * @author: chenz
 * @date: 2018年1月15日 下午5:11:00
 * @version: V1.0
 */
public class OtaUpdateDataTest {

    @Test
    @Ignore
    public void testOta() throws Exception {
        GmmcCommandFacotry facotry = new GmmcCommandFacotry();
        ByteBuf buffer = facotry.createCommand(CommandType.OTA, "862234021042470", 0, null, "v1.0",
                "test.tar.gz", "http://www.incarcloud.com/ota/test.tar.gz");
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        System.out.println(HexStringUtil.toHexString(bytes));
    }

    @Test
    @Ignore
    public void testLength() {
//		String s = "000000v1.0000000v1.0";
//		System.out.println(s.getBytes().length);

        String str = "2323010123FE38363232333430323130343234373001002D1201110B230C000476312E30000B746573745061636B61676500127777772E696E636172636C6F75642E636F6D00";

        byte[] checkBytes = HexStringUtil.parseBytes(str);

        checkBytes = GmmcDataPackUtils.addCheck(checkBytes);
        System.out.println(HexStringUtil.toHexString(checkBytes));
    }

}
