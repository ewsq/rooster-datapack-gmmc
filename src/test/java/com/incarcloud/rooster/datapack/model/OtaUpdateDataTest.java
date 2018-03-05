package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.DataParserGmmc;
import com.incarcloud.rooster.datapack.GmmcCommandFactory;
import com.incarcloud.rooster.datapack.gmmc.model.OtaUpdateData;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.gather.cmd.CommandType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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
        GmmcCommandFactory facotry = new GmmcCommandFactory();
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

    @Test
    @Ignore
    public void otaUpgrade() throws Exception {
        GmmcCommandFactory facotry = new GmmcCommandFactory();
        Object[] args = new Object[6] ;
        args[0] = "862234021042470" ;
        args[1] = 1 ;
        args[2] = "NTBlNzczYmMtNjc2Yi00AA==" ;
        args[3] = "1.0.0" ;
        args[4] = "1.0.0" ;
        args[5] = "https://demo.gmmc.incarcloud.com:51234/files/tbox/d018e2e4-349d-41df-81ab-e5fc4d80eff3.bin" ;
        ByteBuf buffer = facotry.createCommand(CommandType.OTA,args) ;
        byte[] bytes = ByteBufUtil.getBytes(buffer) ;


        System.out.println(HexStringUtil.toHexString(bytes));
    }

    @Test
    @Ignore
    public void decode() throws Exception {
        String str = "2323010123FE38363232333430323130343234373000005003010F311F00086F74615F7465737400086F74615F7465737400342F66696C65732F74626F782F37633564393439312D383334342D343665362D396336312D3134393032653566396633612E62696EA710" ;
        byte[] bytes = HexStringUtil.parseBytes(str) ;

        ProtocolEngine engine = new ProtocolEngine() ;

        OtaUpdateData otaUpdateData = engine.decode(bytes,OtaUpdateData.class) ;

        System.out.println(otaUpdateData);
    }
}
