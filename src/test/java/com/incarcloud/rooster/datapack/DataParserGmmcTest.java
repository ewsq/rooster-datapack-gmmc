package com.incarcloud.rooster.datapack;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.gmmc.model.ActivationRespData;
import com.incarcloud.rooster.datapack.gmmc.model.RunData;
import com.incarcloud.rooster.datapack.gmmc.strategy.impl.RunDataStrategy;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.security.AesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.apache.commons.codec.binary.Hex;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Base64;
import java.util.List;

/**
 * DataParserGmmcTest
 *
 * @author Aaric, created on 2017-11-22T18:02.
 * @since 2.0
 */
public class DataParserGmmcTest {

    /**
     * 应答消息
     */
    @Test
    @Ignore
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

    @Test
    @Ignore
    public void testActivation(){
        String buffStr = "2323010112fe3931313131313131313131313131390000171202180a13194c534241414141414141414141414139399f" ;
        byte[] bytes = HexStringUtil.parseBytes(buffStr);
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(bytes) ;
        DataParserGmmc dataParserGmmc = new DataParserGmmc() ;
        String deviceId = "911111111111119" ;
        String privateKeye = "AKK4Nd+UzZlKinaojsM8NVTpNbn2ghTAcuWhtdWIaBXyLky1c1zMA44s8IggsWVcwrrX0+uOMSk6Ipri0evoFsgUGrRh9korYqZTEHpeZs1o0vMZRZDmcmm8hrz3HP/ADyYAQBcRg/71Y2NUMTNLxgkjNXgggrE6O7xXJUc5QigB";
        String privateKeyn = "AKM0hmWjszvrjEF8peF/23Vth6P2fNXcmNHjqf4QxlSmalt9FNwOIxDDMqFvkBE7NvdB7rpLScVS1JoiOr8+8GPx4VPdqHcqdubKCPBWWe1CPmlSlfKQHZACEfZ6qT0Eq8CP4g+s4jSRw3u0z8U8knL3v3YVG1ScijS9HEei3LIx" ;
        byte[] n = Base64.getDecoder().decode(privateKeyn) ;
        byte[] e = Base64.getDecoder().decode(privateKeye) ;
        dataParserGmmc.setPrivateKey(deviceId, n,e);

        long publicKeye = 65537L ;
        String publicKeyn = "ozSGZaOzO+uMQXyl4X/bdW2Ho/Z81dyY0eOp/hDGVKZqW30U3A4jEMMyoW+QETs290HuuktJxVLUmiI6vz7wY/HhU92odyp25soI8FZZ7UI+aVKV8pAdkAIR9nqpPQSrwI/iD6ziNJHDe7TPxTyScve/dhUbVJyKNL0cR6LcsjE=" ;

        dataParserGmmc.setPublicKey(deviceId,Base64.getDecoder().decode(publicKeyn),publicKeye);
        List<DataPack> dataPacks = dataParserGmmc.extract(buf) ;
        ByteBuf byteBuf = dataParserGmmc.createResponse(dataPacks.get(0),ERespReason.OK) ;
        byte[] buff = ByteBufUtil.getBytes(byteBuf) ;
        System.out.println(HexStringUtil.toHexString(buff));
    }


    @Test
    @Ignore
    public void test() throws Exception {
        String buffStr = "2323010112fe3931313131313131313131313131390000171202180a13194c534241414141414141414141414139399f" ;
        byte[] dataPackBytes = HexStringUtil.parseBytes(buffStr);
        String vinCode = new String(GmmcDataPackUtils.getRange(dataPackBytes, 30, 47));
        byte[] bytes = new byte[20] ;
        dataPackBytes = bytes ;
        System.out.println(HexStringUtil.toHexString(dataPackBytes));
    }

    @Test
    @Ignore
    public void test0() throws Exception {
        String buffStr = "2323010123FE38363232333430323130343234373002006041D317E2BB476B12EDB3CA29CA19AC678CF42A6A9A722B24CDAA8AC247A6A5255F0E63B751C59A70B8BFC345045999388473A9005F54E31779483E2FDB6FE106BC87A312CD23C59F1A431C5BA04B7238222BBF08704227B61346CE5D469D00B132" ;
        buffStr = buffStr.replaceAll(" ","") ;

        String privateKeye = "FuRL8xe9SdA7FYIqmkJN3oDpbh65Qqeqi9pvRPE2U7D6LI3rUh5CbKmGo+H7rvEi26LzI/NaOWGw9hGE6TbPo/P6BHMxSvhanBWLjJJQ9TgERwHZ7ax0Is4qQmULKY/vgxR8kk1bawerdb88Rgq6QzfBub5sipPMDyPnekyr64E=" ;
        String privateKeyn = "AJKfy75w5dGAbDpgemgBAQVfrE93RxcKicuvA8mRy66E2bVYENBm/njLCosf0GLMNFw3nOUkLsE/tVXt3D2Kh5S49a6ppHG67xcm30CqiptDe8WIvyKag2TgpZfHdTiyXuh3GDNJpI9lMRJeWwa6Ruk2502xzIcKKpyvcKQq9Leb" ;
        byte[] bytes = HexStringUtil.parseBytes(buffStr) ;
//        byte[] bytes1 =RsaUtil.encryptByRsaPublic(bytes,"kp/LvnDl0YBsOmB6aAEBBV+sT3dHFwqJy68DyZHLroTZtVgQ0Gb+eMsKix/QYsw0XDec5SQuwT+1Ve3cPYqHlLj1rqmkcbrvFybfQKqKm0N7xYi/IpqDZOCll8d1OLJe6HcYM0mkj2UxEl5bBrpG6TbnTbHMhwoqnK9wpCr0t5s=",65537L);

        String deviceId = "862234021042470" ;
        DataParserGmmc dataParserGmmc = new DataParserGmmc() ;
        dataParserGmmc.setPrivateKey(deviceId,Base64.getDecoder().decode(privateKeyn),Base64.getDecoder().decode(privateKeye));
        dataParserGmmc.setSecurityKey(deviceId,Base64.getDecoder().decode("MjBkN2RlZDMtMzg5OC00AA=="));
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(bytes) ;

        List<DataPack> dataPacks = dataParserGmmc.extract(buf) ;


        List<DataPackTarget> dataPackTargets = dataParserGmmc.extractBody(dataPacks.get(0)) ;

        System.out.println(dataPackTargets);
//        byte[] rsa = RsaUtil.decryptByRsaPrivate(bytes1,privateKeyn,privateKeye) ;
//        System.out.println(HexStringUtil.toHexString(rsa));
    }

    @Test
    @Ignore
    public void test1(){
        String str = "A3348665A3B33BEB8C417CA5E17FDB756D87A3F67CD5DC98D1E3A9FE10C654A66A5B7D14DC0E2310C332A16F90113B36F741EEBA4B49C552D49A223ABF3EF063F1E153DDA8772A76E6CA08F05659ED423E695295F2901D900211F67AA93D04ABC08FE20FACE23491C37BB4CFC53C9272F7BF76151B549C8A34BD1C47A2DCB231" ;

        byte[] bytes = Base64.getDecoder().decode(str) ;

        ProtocolEngine engine = new ProtocolEngine();
        ActivationRespData activationRespData = null;
        try {
            activationRespData = engine.decode(HexStringUtil.parseBytes("23230101120139313131313131313131313131313900008A12021C0A033900010001A3348665A3B33BEB8C417CA5E17FDB756D87A3F67CD5DC98D1E3A9FE10C654A66A5B7D14DC0E2310C332A16F90113B36F741EEBA4B49C552D49A223ABF3EF063F1E153DDA8772A76E6CA08F05659ED423E695295F2901D900211F67AA93D04ABC08FE20FACE23491C37BB4CFC53C9272F7BF76151B549C8A34BD1C47A2DCB23152"),ActivationRespData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] ints = activationRespData.getPublicKeyModulusBytes() ;

        System.out.println(GmmcDataPackUtils.getBase64OfInt(ints));

    }

    @Test
    public void test2(){
        String str = "38 38 32 32 36 31 31 61 2D 38 63 65 30 2D 34 00" ;
        str = str.replaceAll(" ","");
        byte[] bytes = HexStringUtil.parseBytes(str) ;

        int[] ints = GmmcDataPackUtils.coverToIntArray(bytes) ;

        System.out.println(GmmcDataPackUtils.getBase64OfInt(ints));
    }

    @Test
    @Ignore
    public void securityKey() throws Exception {
        String str = "12 02 1C 0E 01 2D 01 01 01 27 10 4E 20 00 01 00 00 1E 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 13 88 02 01 01 00 14 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 50 03 01 01 01 02 58 00 00 5B A0 03 E8 01 2C 50 01 0E EA 60 C8 00 14 DC DC DC DC 01 01 01 01 01 01 01 01 01 01 0A 19 01 2C 01 00 00 04 01 01 02 50 03 E8 00 64 5A 00 C8 00 1E 07 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 08 01 03 13 88 01 04 13 88 01 02 50 01 03 50" ;
        str = str.replaceAll(" ","");
        byte[] bytes = HexStringUtil.parseBytes(str) ;

        String securityKey = "NGVlMTVmYTItZTU3Mi00AA==" ;
        byte[] securityByte = Base64.getDecoder().decode(securityKey) ;

        System.out.println(HexStringUtil.toHexString(bytes));

        byte[] aes = AesUtil.encrypt(bytes,securityKey) ;

        byte[] aes2 = AesUtil.encrypt(bytes,securityKey) ;

        System.out.println(HexStringUtil.toHexString(aes));

        System.out.println(HexStringUtil.toHexString(aes2));

    }


    @Test
    @Ignore
    public void engine() throws Exception {
        String str = "2323020102FE3836323233343032313034323437300000C512021C11132701010127104E20000100001E1388138813881388138813881388138813881388138813881388138813881388138813881388138813881388138813881388138813881388138813880201010014505050505050505050505050505050505050505003010101025800005BA003E8012C50010EEA60C80014DCDCDCDC010101010101010101010A19012C010000040101025003E800645A00C8001E0701000000000000000000000000000000000000000008010313880104138801025001035070" ;
        byte[] bytes = HexStringUtil.parseBytes(str) ;

        RunDataStrategy strategy = new RunDataStrategy() ;
        DataPack dataPack = new DataPack("test", "test", "test");
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(bytes);
        dataPack.setBuf(buf);
        List<DataPackTarget> list = strategy.decode(dataPack);

        System.out.println(list);
    }

}
