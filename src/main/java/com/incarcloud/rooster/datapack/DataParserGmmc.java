package com.incarcloud.rooster.datapack;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.gmmc.model.*;
import com.incarcloud.rooster.datapack.gmmc.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.gmmc.strategy.impl.*;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.security.AesUtil;
import com.incarcloud.rooster.security.RsaUtil;
import com.incarcloud.rooster.share.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * GMMC设备协议数据包解析器
 *
 * @author Aaric, created on 2017-11-22T17:54.
 * @since 2.0
 */
public class DataParserGmmc implements IDataParser {

    private static ConcurrentMap<String,SecurityData> keyMap = new ConcurrentHashMap<>() ;

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

        //解密逻辑处理
        return decryption(dataPackList) ;
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
        // 获取解析报文并进行校验,报文校验不通过返回null
        byte[] dataPackBytes = GmmcDataPackUtils.readBytes(buffer, buffer.readableBytes());
        // 判断报文是否为空
        if (null != dataPackBytes) {
            // 获取命令ID
            int cmdFlag = dataPackBytes[4] & 0xFF;

            if (null != dataPackBytes) {
                Map<String, Object> metaDataMap = new HashMap<>();
                // 协议版本
                metaDataMap.put(Constants.MetaDataMapKey.PROTOCOL, PROTOCOL_PREFIX + PROTOCOL_VERSION);
                // 获取TBOX硬件的IMEI号码，由 15位字码构成，字码应符合GB16735 中 4.5 的规定
                String imei = new String(GmmcDataPackUtils.getRange(dataPackBytes, 6, 21));
                metaDataMap.put(Constants.MetaDataMapKey.DEVICE_ID, imei);
                // 判断是否是登入报文,只有登入报文才有vin码，其他报文只有imei
                switch (cmdFlag){
                    case 0x01 : //登入
                        /**
                         * 车辆识别码(vin)是识别的唯一标识，由17位字码构成。前三位补0
                         */
                        String vin = new String(GmmcDataPackUtils.getRange(dataPackBytes, 180, 197));
                        metaDataMap.put(Constants.MetaDataMapKey.VIN, vin.trim());
                        metaDataMap.put(Constants.MetaDataMapKey.PACK_TYPE,Constants.PackType.LOGIN) ;
                        break;
                    case 0x12 : //激活
                        /**
                         * 车辆识别码(vin)是识别的唯一标识，由17位字码构成。前三位补0
                         */
                        String vinCode = new String(GmmcDataPackUtils.getRange(dataPackBytes, 30, 47));
                        metaDataMap.put(Constants.MetaDataMapKey.VIN, vinCode.trim());
                        metaDataMap.put(Constants.MetaDataMapKey.PACK_TYPE,Constants.PackType.ACTIVATE) ;
                        break;
                    case 0x05 : //登出
                        metaDataMap.put(Constants.MetaDataMapKey.PACK_TYPE,Constants.PackType.LOGOUT) ;
                        break;
                    default :
                        metaDataMap.put(Constants.MetaDataMapKey.PACK_TYPE,Constants.PackType.NORMAL) ;
                        break;
                }
                return metaDataMap;
            }
        }
        return null;
    }


    @Override
    public void setPrivateKey(String deviceId, byte[] n, byte[] e) {
        SecurityData securityData = keyMap.get(deviceId) ;
        if (null == securityData) securityData = new SecurityData() ;
        securityData.setPrivateKeyModulusBytes(n);
        securityData.setPrivateKeyExponentBytes(e);
        keyMap.put(deviceId,securityData) ;
    }

    @Override
    public void setPublicKey(String deviceId, byte[] n, long e) {
        SecurityData securityData = keyMap.get(deviceId) ;
        if (null == securityData) securityData = new SecurityData() ;
        securityData.setPublicKeyModulusBytes(n);
        securityData.setPublicKeyExponent(e);
        keyMap.put(deviceId,securityData) ;
    }

    @Override
    public byte[] getSecurityKey(String deviceId) {
        try {
            SecurityData securityData = keyMap.get(deviceId) ;
            if (null == securityData) return null ;
            return securityData.getSecurityKey() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecurityData getSecurityData(String deviceId){
        SecurityData securityData = keyMap.get(deviceId) ;
        return securityData ;
    }

    /**
     * 设置 securityKey
     * @param deviceId
     * @param securityKey
     */
    private void setSecurityKey(String deviceId,byte[] securityKey){
        SecurityData securityData = keyMap.get(deviceId) ;
        if (null == securityData) securityData = new SecurityData() ;
        securityData.setSecurityKey(securityKey);
        keyMap.put(deviceId,securityData) ;
    }

    /**
     * 初解数据解密
     * @param dataPacks
     * @return
     */
    private List<DataPack> decryption(List<DataPack> dataPacks){

        List<DataPack> dataPackList = new ArrayList<>() ;
        // 解析器
        ProtocolEngine engine = new ProtocolEngine();

        if (null != dataPacks){
            for (int x = 0 ; x < dataPacks.size() ; x ++){
                ByteBuf byteBuf = Unpooled.buffer() ;
                DataPack dataPack = dataPacks.get(x) ;
                int index = 0 ;
                byte[] bytes = dataPack.getDataBytes() ;
                // 获取协议头
                byte[] header = new byte[24];

                System.arraycopy(bytes,0,header,0,24);

                index += header.length ;
                // 从协议头中判断是否需要加解密
                Header headerData = null;
                try {
                    headerData = engine.decode(header, Header.class);// 包头
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //写入Header
                byteBuf.writeBytes(header) ;

                // 获取包体长度
                byte[] length = new byte[2];
                System.arraycopy(header, 22, length, 0, 2);

                int bit32 = 0;
                bit32 = length[0] & 0xFF;
                bit32 = bit32 << 8;
                bit32 |= (length[1] & 0xFF);

                String deviceId = headerData.getImei() ;
                SecurityData securityData = keyMap.get(deviceId) ;

                //0x00：数据不加密；0x01：数据经过RSA算法加密；0x02：数据经过AES加密算法加密，0xFF：无效数据；其他预留
                int encryptType = headerData.getEncryptType() ;

                if (null == securityData && encryptType != 0x00) continue;

                try {
                    //RSA解密 128 为一组
                    if (encryptType == 0x01){
                        int group = bit32/128 + bit32%128 > 0 ? 1 : 0;

                        byte[] privateKeyModulusBytes = securityData.getPrivateKeyModulusBytes() ;
                        byte[] privateKeyExponentBytes = securityData.getPrivateKeyExponentBytes() ;

                        for (int i = 0 ; i < group ; i++){
                            byte[] dataByte = new byte[128] ;
                            System.arraycopy(bytes,index,dataByte,0,128);
                            index += 128 ;
                            byte[] rsaByte = RsaUtil.decryptByRsaPrivate(dataByte,privateKeyModulusBytes,privateKeyExponentBytes);
                            //写入数据单元
                            byteBuf.writeBytes(rsaByte);
                        }

                    }else if(encryptType == 0x02){ //AES 加密
                        byte[] dataByte = new byte[bit32] ;
                        System.arraycopy(bytes,index,dataByte,0,bit32);
                        byte[] securityKey = securityData.getSecurityKey() ;
                        byte[] aesByte = AesUtil.decrypt(dataByte,securityKey) ;
                        //写入数据单元
                        byteBuf.writeBytes(aesByte) ;
                    }else{

                        DataPack data = new DataPack(PROTOCOL_GROUP, PROTOCOL_NAME, PROTOCOL_VERSION);
                        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
                        data.setBuf(buf);
                        dataPackList.add(data) ;
                        //释放byteBuff
                        dataPack.freeBuf();
                        continue;
                    }

                    //补位--检验码
                    byteBuf.writeByte((byte)0x01) ;

                    byte[] dataBytes = ByteBufUtil.getBytes(byteBuf);
                    //设置为未加密
                    dataBytes[21] = 0x00 ;
                    //数据单元长度+检验码处理
                    GmmcDataPackUtils.addCheck(dataBytes);

                    DataPack data = new DataPack(PROTOCOL_GROUP, PROTOCOL_NAME, PROTOCOL_VERSION);
                    ByteBuf buf = Unpooled.wrappedBuffer(dataBytes);
                    data.setBuf(buf);

                    dataPackList.add(data);

                    /**
                     * 判断是否登入
                     * 将SecurityKey存入缓存
                     */
                    if (headerData.getCmdFlag() == 0x01){
                        byte[] securityKey = GmmcDataPackUtils.getRange(dataBytes, 32, 48) ;
                        setSecurityKey(deviceId,securityKey) ;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //释放资源
                    byteBuf.release();
                }
                //释放byteBuff
                dataPack.freeBuf();
            }
        }

        return dataPackList ;
    }

}
