package com.incarcloud.rooster.datapack;

import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.security.RsaUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
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
    public void testActivation(){
        String buffStr = "2323010112fe3931313131313131313131313131390000171202180a13194c534241414141414141414141414139399f" ;
        byte[] bytes = HexStringUtil.parseBytes(buffStr);
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(bytes) ;
        DataParserGmmc dataParserGmmc = new DataParserGmmc() ;
        String deviceId = "911111111111119" ;
        String privateKeye = "bbrxPq894DpXs7XgH6UgyYcB7xri+4UiVsNWFXJwwrA+nf92zbZIfzu1pyyiaCNRvt7hH8Pvnq/vtSeBDptUlR77pe71kdDcosI5Le7yjgP/Et0epHqWnusKpcqSshcJfP+u+tS61BljAuN9f9XSR+k2p0YhvTQJJEvaD9JQQrE=" ;
        String privateKeyn = "ALG7YmTar/YHt+lPGSCkZsqWORuG/ebrukbST/O0KrODi4XaWONYjY43yKUfM6UufU/wNT0jL7v4WM/FbTqNQzBLNW7ut+hCYbUZLYwgGsOagla/OrXwN8Puy6F+f0OxVs2wyVIYDHN4PreFnxG7C28puhz65nKvk+7lxx0oZUWj" ;
//        dataParserGmmc.setPrivateKey(deviceId, Base64.getDecoder().decode(privateKeyn),Base64.getDecoder().decode(privateKeye));

        List<DataPack> dataPacks = dataParserGmmc.extract(buf) ;
        System.out.println(dataPacks);
    }


    @Test
    public void test() throws Exception {
        String buffStr = "29a0a7c85dba5cba9a636eee2cf9fa3372de36b8a61b4790b9d27a37984a99038c69afb9562979ab91bdd3a010c7d7affec9788e1528bbf279ac4d913c75d92cdfcf7232eace5fbc7e54b8a0fac492e9845e26468a2d3fafe96a198176ee3a34fb43d2fbf9efe30a717f1dac819cbe1198490d81baccdfd9b82cd20b06ea92fa" ;
        byte[] rsabb = HexStringUtil.parseBytes(buffStr);
        byte[] content = RsaUtil.decryptByRsaPrivate(rsabb, "ALG7YmTar/YHt+lPGSCkZsqWORuG/ebrukbST/O0KrODi4XaWONYjY43yKUfM6UufU/wNT0jL7v4WM/FbTqNQzBLNW7ut+hCYbUZLYwgGsOagla/OrXwN8Puy6F+f0OxVs2wyVIYDHN4PreFnxG7C28puhz65nKvk+7lxx0oZUWj", "bbrxPq894DpXs7XgH6UgyYcB7xri+4UiVsNWFXJwwrA+nf92zbZIfzu1pyyiaCNRvt7hH8Pvnq/vtSeBDptUlR77pe71kdDcosI5Le7yjgP/Et0epHqWnusKpcqSshcJfP+u+tS61BljAuN9f9XSR+k2p0YhvTQJJEvaD9JQQrE=");
        System.err.println(ByteBufUtil.hexDump(content));

    }
}
