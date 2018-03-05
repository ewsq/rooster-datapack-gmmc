package com.incarcloud.rooster.datapack;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.gmmc.model.ActivationData;
import com.incarcloud.rooster.datapack.gmmc.model.Header;
import com.incarcloud.rooster.datapack.gmmc.model.LoginData;
import com.incarcloud.rooster.datapack.gmmc.model.Tail;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.security.AesUtil;
import com.incarcloud.rooster.security.RsaUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Base64;

/**
 * DataGenerateTest
 */
public class PackDataGenerateTest {

    @Test
    @Ignore
    public void testGenerateActivationData() throws Exception {
        // 包头
        Header header = new Header();
        header.setCarFlag(0x01);// 车辆类型标识 0x01燃油车 ；0x02 新能源车
        header.setCarType(0x01);// 车型 0x01 ZC；0xRE NS；0x03 NS
        header.setCmdFlag(0x12);// 命令标识-0x12 激活
        header.setResponeFlag(0xFE);// 应答标识 命令包
        header.setImei("911111111111119");// imei
        header.setEncryptType(0x00);// 加密方式
        header.setLength(0x00);// 数据单元长度

        // 包尾
        Tail tail = new Tail();

        // 模拟激活报文
        ActivationData activationData = new ActivationData();
        activationData.setHeader(header);// 设置头部信息
        activationData.setTail(tail);// 设置尾部信息

        // 正文信息
        activationData.setGatherTime(System.currentTimeMillis());
        activationData.setVin("LSBAAAAAAAAAAAA99");

        ProtocolEngine engine = new ProtocolEngine();
        byte[] responseBytes = GmmcDataPackUtils.addCheck(engine.encode(activationData));
        System.out.println(ByteBufUtil.hexDump(responseBytes));
    }

    @Test
    @Ignore
    public void testGenerateLoginData() throws Exception {
        // 包头
        Header header = new Header();
        header.setCarFlag(0x01);// 车辆类型标识 0x01燃油车 ；0x02 新能源车
        header.setCarType(0x01);// 车型 0x01 ZC；0xRE NS；0x03 NS
        header.setCmdFlag(0x01);// 命令标识-0x01 车辆登入
        header.setResponeFlag(0xFE);// 应答标识 命令包
        header.setImei("911111111111119");// imei
        header.setEncryptType(0x00);// 加密方式
        header.setLength(0x00);// 数据单元长度

        // 包尾
        Tail tail = new Tail();

        // 模拟登陆报文
        LoginData loginData = new LoginData();
        loginData.setHeader(header);// 设置头部信息
        loginData.setTail(tail);// 设置尾部信息

        // 正文信息
        loginData.setGatherTime(System.currentTimeMillis());
        loginData.setSerialNumber(1);
        byte[] aesBytes = AesUtil.generateAesSecret();
        int[] aesInts = new int[aesBytes.length];
        for (int i = 0; i < aesBytes.length; i++) {
            aesInts[i] = aesBytes[i];
        }
        loginData.setSecurityKey(aesInts);
        loginData.setSoftwareVersion("0000000000000000V1.0");
        loginData.setVin("LSBAAAAAAAAAAAA99");
        loginData.setElectricalCount(0);
        loginData.setElectricalLenth(0);
        loginData.setElectricalSysCode("");

        // 包头[6+15+3]

        ProtocolEngine engine = new ProtocolEngine();
        byte[] data = engine.encode(loginData);

        byte[] aa = new byte[24];
        byte[] bb = new byte[data.length-25];
        byte check = data[data.length-1];
        System.arraycopy(data, 0, aa, 0, 24);
        System.arraycopy(data, 24, bb, 0, data.length-25);
        System.out.println("-----------------------------data");
        /*System.out.println(ByteBufUtil.hexDump(aa));
        System.out.println(ByteBufUtil.hexDump(bb));
        System.out.println(ByteBufUtil.hexDump(new byte[]{check}));*/
        System.out.println(ByteBufUtil.hexDump(GmmcDataPackUtils.addCheck(data)));

        byte[] rsapn = Base64.getDecoder().decode("sbtiZNqv9ge36U8ZIKRmypY5G4b95uu6RtJP87Qqs4OLhdpY41iNjjfIpR8zpS59T/A1PSMvu/hYz8VtOo1DMEs1bu636EJhtRktjCAaw5qCVr86tfA3w+7LoX5/Q7FWzbDJUhgMc3g+t4WfEbsLbym6HPrmcq+T7uXHHShlRaM=");
        long rsape = 65537;
        byte[] rsabb = RsaUtil.encryptByRsaPublic(bb, rsapn, rsape);
        System.out.println("-----------------------------rsabb");
        System.out.println(ByteBufUtil.hexDump(rsabb));
        System.out.println(rsabb.length);

        byte[] content = RsaUtil.decryptByRsaPrivate(rsabb, "ALG7YmTar/YHt+lPGSCkZsqWORuG/ebrukbST/O0KrODi4XaWONYjY43yKUfM6UufU/wNT0jL7v4WM/FbTqNQzBLNW7ut+hCYbUZLYwgGsOagla/OrXwN8Puy6F+f0OxVs2wyVIYDHN4PreFnxG7C28puhz65nKvk+7lxx0oZUWj", "bbrxPq894DpXs7XgH6UgyYcB7xri+4UiVsNWFXJwwrA+nf92zbZIfzu1pyyiaCNRvt7hH8Pvnq/vtSeBDptUlR77pe71kdDcosI5Le7yjgP/Et0epHqWnusKpcqSshcJfP+u+tS61BljAuN9f9XSR+k2p0YhvTQJJEvaD9JQQrE=");
        System.err.println(ByteBufUtil.hexDump(content));

        System.out.println("-----------------------------rsa");
        ByteBuf buffer = Unpooled.buffer();
        aa[21] = 0x01; // 0x01：数据经过RSA算法加密；
        buffer.writeBytes(aa);
        buffer.writeBytes(rsabb);
        buffer.writeBytes(new byte[]{0x00});

        System.out.println(ByteBufUtil.hexDump(GmmcDataPackUtils.addCheck(ByteBufUtil.getBytes(buffer))));
        ReferenceCountUtil.release(rsabb);
    }
}
