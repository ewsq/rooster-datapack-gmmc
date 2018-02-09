package com.incarcloud.rooster.datapack;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.model.*;
import com.incarcloud.rooster.datapack.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.strategy.impl.*;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.security.AesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

import java.util.*;

/**
 * GMMC设备协议数据包解析器
 *
 * @author Aaric, created on 2017-11-22T17:54.
 * @since 2.0
 */
public class DataParserGmmc implements IDataParser {

    /**
     * 协议分组和名称
     */
    public static final String PROTOCOL_GROUP = "china";
    public static final String PROTOCOL_NAME = "gmmc";
    public static final String PROTOCOL_VERSION = "2017.11";
    public static final String PROTOCOL_PREFIX = PROTOCOL_GROUP + "-" + PROTOCOL_NAME + "-";

    // 国标协议最小长度
    private static final int GB_LENGTH = 25;

    static {
        /**
         * 声明数据包版本与解析器类关系
         */
        DataParserManager.register(PROTOCOL_PREFIX + PROTOCOL_VERSION, DataParserGmmc.class);
    }

    /**
     * 数据包准许最大容量2M
     */
    private static final int DISCARDS_MAX_LENGTH = 1024 * 1024 * 2;

    /**
     * ## 广汽三菱数据包格式,基于国标协议<br>
     * ### 协议应采用大端模式的网络字节序来传递字和双字 具体内容见协议[广汽三菱车联网-车载终端通信协议及数据]<br>
     */
    @Override
    public List<DataPack> extract(ByteBuf buffer) {
        // TODO 如果报文被加密，将报文解密后，生成新的报文返回
        // TODO 登陆报文使用privateKey解密，其他报文使用securityKey解密
        DataPack dataPack;
        List<DataPack> dataPackList = new ArrayList<>();

        // 长度大于2M的数据直接抛弃(恶意数据)
        if (DISCARDS_MAX_LENGTH < buffer.readableBytes()) {
            buffer.clear();
        }

        // 遍历
        int offset1;
        while (buffer.isReadable()) {
            offset1 = buffer.readerIndex();
            // 查找协议头标识--->0x23开头
            if (buffer.getByte(offset1) == (byte) 0x23 && buffer.getByte(offset1 + 1) == (byte) 0x23) {
                if (buffer.readableBytes() > GB_LENGTH) {
                    // 记录读取的位置
                    buffer.markReaderIndex();
                    // 获取协议头
                    byte[] header = new byte[24];
                    // 写入协议头数据
                    buffer.readBytes(header);
                    // 获取包体长度
                    byte[] length = new byte[2];
                    System.arraycopy(header, 22, length, 0, 2);

                    int bit32 = 0;
                    bit32 = length[0] & 0xFF;
                    bit32 = bit32 << 8;
                    bit32 |= (length[1] & 0xFF);

                    if (buffer.readableBytes() < bit32) {
                        buffer.resetReaderIndex();
                        break;
                    }

                    // 重置读指针的位置,最终读取的数据需要包含头部信息
                    buffer.resetReaderIndex();
                    // 数据包总长度为包头24+包体长度+包尾1
                    ByteBuf data = buffer.readBytes(bit32 + 25);
                    // 创建存储数组
                    byte[] dataBytes = new byte[bit32 + 25];
                    // 读取数据到数组
                    data.readBytes(dataBytes);
                    // 数据包校验
                    boolean check = false;
                    int offset = 0;// 起始位置
                    int packetSize = dataBytes.length;// 数据包长度
                    /**
                     * 采用BCC（异或校验）法，<br>
                     * 校验范围从命令单元的第一个字节开始，同后一字节异或，<br>
                     * 直到校验码前一字节为止，校验码占用一个字节<br>
                     */
                    if (dataBytes[offset] == (byte) 0x23 && dataBytes[offset + 1] == (byte) 0x23) {
                        int crc = dataBytes[offset + 4] & 0xFF;
                        for (int i = offset + 5; i < offset + packetSize - 1; i++) {
                            crc = crc ^ (dataBytes[i] & 0xFF);
                        }
                        if (crc != (dataBytes[offset + packetSize - 1] & 0xFF)) {
                            check = false;
                        } else {
                            check = true;
                        }
                    }

                    // 打包
                    if (check) {
                        dataPack = new DataPack(PROTOCOL_GROUP, PROTOCOL_NAME, PROTOCOL_VERSION);
                        ByteBuf buf = Unpooled.wrappedBuffer(dataBytes);
                        dataPack.setBuf(buf);
                        dataPackList.add(dataPack);
                    }
                }
            } else {
                // 协议头不符合，跳过这个字节
                buffer.skipBytes(1);
            }
        }
        // 扔掉已读数据
        buffer.discardReadBytes();
        return dataPackList;
    }

    /**
     * 应答
     */
    @Override
    public ByteBuf createResponse(DataPack requestPack, ERespReason reason) {
        // 解析器
        ProtocolEngine engine = new ProtocolEngine();
        if (null != requestPack && null != reason) {
            // 原始数据
            byte[] dataPackBytes = Base64.getDecoder().decode(requestPack.getDataB64());
            if (null != dataPackBytes) {
                try {
                    // 获取协议头部信息
                    byte[] headerBytes = GmmcDataPackUtils.getRange(dataPackBytes, 0, 24);
                    Header header = engine.decode(headerBytes, Header.class);// 包头
                    Tail tail = new Tail();// 包尾
                    byte[] responseBytes = {};
                    /**
                     * 获取命令标识,根据命令标识返回对于的消息<br>
                     * 0x11 电检 通用应答 <br>
                     * 0x12 激活 通用应答 <br>
                     * 0x13 PublicKey重置 通用应答 <br>
                     * 0x01 车辆登入 通用应答<br>
                     * 0x05 车辆登出 通用应答<br>
                     * 0x08 终端校时 通用应答<br>
                     * 0x09 车辆告警信息上报 通用应答<br>
                     * 0x21 参数设置命令 返回参数列表<br>
                     * 0x22 参数设置完成 通用应答<br>
                     * 0x23 OTA升级 返回升级信息<br>
                     * 0x24 OTA升级完成 通用应答<br>
                     */
                    Integer cmdFlag = dataPackBytes[4] & 0xFF;

                    if (0x11 == cmdFlag || 0x12 == cmdFlag || 0x13 == cmdFlag || 0x01 == cmdFlag || 0x05 == cmdFlag
                            || 0x08 == cmdFlag || 0x09 == cmdFlag || 0x22 == cmdFlag || 0x24 == cmdFlag) {

                        // 设置应答标识 0x01 成功
                        header.setResponeFlag(0x01);
                        // 通用应答
                        CommonRespData resp = new CommonRespData();

                        resp.setHeader(header);// 消息头
                        resp.setGatherTime(System.currentTimeMillis());// 设置应答时间
                        resp.setTail(tail);// 包尾

                        responseBytes = engine.encode(resp);// 生成应答包byte数组
                        GmmcDataPackUtils.addCheck(responseBytes);// 添加校验码和包体长度
                    } else if (0x21 == cmdFlag) { // TODO: 参数设置命令 返回参数设置列表

                    } else if (0x23 == cmdFlag) { // TODO: ota升级 返回ota升级信息
                        OtaUpdateData ota = engine.decode(dataPackBytes, OtaUpdateData.class);
                        // TODO 软件版本校验，校验当前tbox是否需要升级
                        // 设置应答标识 0x01 成功
                        header.setResponeFlag(0x01);
                        OtaUpdateResp resp = new OtaUpdateResp();
                        resp.setHeader(header);
                        resp.setFlag(0);// 是否需要更新0 不需要 1 需要
                        resp.setSoftwareVersion(ota.getSoftwareVersion());// 软件版本
                        resp.setUpdatePackageName("test");
                        resp.setUrl("http://www.incarcloud.com/");
                        resp.setTail(tail);

                        responseBytes = engine.encode(resp);// 生成应答包byte数组
                        GmmcDataPackUtils.addCheck(responseBytes);// 添加校验码和包体长度
                    }
                    // 返回应答消息
                    return Unpooled.wrappedBuffer(responseBytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // TODO
        return null;
    }

    /**
     * 销毁应答
     */
    @Override
    public void destroyResponse(ByteBuf responseBuf) {
        if (null != responseBuf) {
            ReferenceCountUtil.release(responseBuf);
        }
    }

    /**
     * 解析完整数据包
     */
    @Override
    public List<DataPackTarget> extractBody(DataPack dataPack) {
        // TODO 解析完整数据包
        List<DataPackTarget> dataPackTargetList = null;
        // 获取完整的数据包
        byte[] dataPackBytes = dataPack.getDataBytes();
        if (null != dataPackBytes) {
            dataPackTargetList = new ArrayList<>();
            // 获取命令ID
            Integer cmdFlag = dataPackBytes[4] & 0xFF;

            switch (cmdFlag) {
                case 0x01:// 车辆登入
                    GmmcDataPackUtils.debug("=====车辆登入=====");
                    // 解析登录信息
                    IDataPackStrategy loginDataStrategy = new LoginDataStrategy();
                    dataPackTargetList = loginDataStrategy.decode(dataPack);
                    break;

                case 0x02:// 车辆运行信息上报
                    GmmcDataPackUtils.debug("=====车辆运行信息上报=====");
                    // 解析车辆运行信息上报
                    IDataPackStrategy runDataStrategy = new RunDataStrategy();
                    dataPackTargetList = runDataStrategy.decode(dataPack);
                    break;

                case 0x03:// 心跳
                    GmmcDataPackUtils.debug("=====心跳=====");
                    // 解析登录信息
                    IDataPackStrategy heartbeatStrategy = new HeartbeatStrategy();
                    dataPackTargetList = heartbeatStrategy.decode(dataPack);
                    break;

                case 0x04:// 补发信息上报
                    GmmcDataPackUtils.debug("=====补发信息上报=====");
                    // 解析补发信息
                    IDataPackStrategy reissueDataStrategy = new RunDataStrategy();
                    dataPackTargetList = reissueDataStrategy.decode(dataPack);
                    break;

                case 0x05:// 车辆登出
                    GmmcDataPackUtils.debug("=====车辆登出=====");
                    // 解析登录信息
                    IDataPackStrategy logoutDataStrategy = new LogoutDataStrategy();
                    dataPackTargetList = logoutDataStrategy.decode(dataPack);
                    break;

                case 0x09:// 车辆告警信息上报
                    GmmcDataPackUtils.debug("=====车辆告警信息上报=====");
                    // 解析登录信息
                    IDataPackStrategy alarmDataStrategy = new AlarmDataStrategy();
                    dataPackTargetList = alarmDataStrategy.decode(dataPack);
                    break;

                case 0x11:// 电检
                    GmmcDataPackUtils.debug("=====电检=====");
                    // 解析电检信息
                    IDataPackStrategy electricalCheckStrategy = new ElectricalCheckStrategy();
                    dataPackTargetList = electricalCheckStrategy.decode(dataPack);
                    break;

                case 0x12:// 激活
                    // 解析设备激活信息
                    IDataPackStrategy activationStrategy = new ActivationStrategy();
                    activationStrategy.decode(dataPack);
                    break;

                case 0x13:// PublicKey重置
                    GmmcDataPackUtils.debug("=====PublicKey重置=====");
                    // 解析PublicKey重置信息
                    IDataPackStrategy publicKeyStrategy = new PublicKeyResetStrategy();
                    publicKeyStrategy.decode(dataPack);
                    break;

                case 0x0B:// 车辆行程数据
                    GmmcDataPackUtils.debug("=====车辆行程数据=====");
                    IDataPackStrategy tripDataStrategy = new TripDataStrategy();
                    dataPackTargetList = tripDataStrategy.decode(dataPack);
                    break;
                case 0x23:// ota升级

                    break;
                case 0x24:// ota升级完成

                    break;
            }
        }

        return dataPackTargetList;
    }

    /**
     * 解析数据包获取vin/设备号/协议
     */
    @Override
    public Map<String, Object> getMetaData(ByteBuf buffer) {
        // TODO 必须返回protocol, algorithm, deviceId，vin可以不返回
        // 获取解析报文并进行校验,报文校验不通过返回null
        byte[] dataPackBytes = GmmcDataPackUtils.readBytes(buffer, buffer.readableBytes());
        // 判断报文是否为空
        if (null != dataPackBytes) {
            // 获取命令ID
            int cmdFlag = dataPackBytes[4] & 0xFF;

            if (null != dataPackBytes) {
                Map<String, Object> metaDataMap = new HashMap<>();
                // 协议版本
                metaDataMap.put("protocol", PROTOCOL_PREFIX + PROTOCOL_VERSION);
                // 获取TBOX硬件的IMEI号码，由 15位字码构成，字码应符合GB16735 中 4.5 的规定
                String imei = new String(GmmcDataPackUtils.getRange(dataPackBytes, 6, 21));
                metaDataMap.put("imei", imei);
                // 判断是否是登入报文,只有登入报文才有vin码，其他报文只有imei
                if (1 == cmdFlag) {
                    /**
                     * 车辆识别码(vin)是识别的唯一标识，由17位字码构成。前三位补0
                     */
                    String vin = new String(GmmcDataPackUtils.getRange(dataPackBytes, 180, 197));
                    metaDataMap.put("vin", vin.trim());
                }
                return metaDataMap;
            }
        }
        return null;
    }

    @Override
    public void setPrivateKey(byte[] n, byte[] e) {
        // TODO 缓存RSA私钥
    }

    @Override
    public void setPublicKey(byte[] n, long e) {
        // TODO 缓存RSA公钥
    }

    @Override
    public byte[] getSecurityKey() {
        // TODO 删除以下代码，返回缓存的AES密钥信息
        try {
            return AesUtil.generateAesSecret();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
