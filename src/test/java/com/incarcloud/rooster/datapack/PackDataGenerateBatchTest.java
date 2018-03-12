package com.incarcloud.rooster.datapack;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.gmmc.model.*;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.security.AesUtil;
import com.incarcloud.rooster.security.RsaUtil;
import io.netty.buffer.ByteBufUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * PackDataGenerateBatchTest
 */
public class PackDataGenerateBatchTest {

    private static int ACTIVATION_CMD_FLAG = 0x12;

    private static int LOGIN_CMD_FLAG = 0x01;

    private static int LOGOUT_CMD_FLAG = 0x05;

    private static int TRIP_CMD_FLAG = 0x0B;

    private static int RUN_CMD_FLAG = 0x02 ;


    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public void activation(){

    }

    @Test
    public void login() throws Exception {
        String vin = "LSBAAAAAAAAAAAA99" ;
        String deviceId = "911111111111119" ;
        int serialNumber = 1 ;
        byte[] loginByte = loginDataGenerate(vin,deviceId,serialNumber);

        long publicKeye = 65537L ;
        String publicKeyn = "xO1TYCj3GHWRxQ4RzgsFKhJISwBmRbu1xewbGev7aPHtbjcbvPkDXkrzaT3XkxDK3F8RFdFwx9ZFr2wiaqYy29p8ggRT+bLmeA9NcjY/fstiRik9EMt/3PePJuviU9bNVWkaS9zn2pKFZKIjjlmr863X86fF9vuRxvdgv0mr1ZE=" ;

        byte[] rsaBytes = encryptRsa(loginByte,publicKeyn,publicKeye) ;

        System.out.println(HexStringUtil.toHexString(rsaBytes));

    }

    @Test
    @Ignore
    public void generateRunData() throws Exception {
        String vin = "LSBAAAAAAAAAAAA99" ;
        String deviceId = "911111111111119" ;
        byte[] runBuffer = runData(vin,deviceId) ;

        String securityKey = "6iAaO16+WmTjZ09pR/jzhw==" ;

        byte[] aesBytes = encryptAes(runBuffer, Base64.getDecoder().decode(securityKey)) ;

        System.out.println(HexStringUtil.toHexString(aesBytes));
    }

    /**
     * 车辆激活
     * @param vin
     * @param imei
     * @return
     * @throws Exception
     */
    public String activationDataGenerate(String vin, String imei) throws Exception {
        // 包头
        Header header = getHeader(imei, ACTIVATION_CMD_FLAG);

        // 包尾
        Tail tail = new Tail();

        // 模拟激活报文
        ActivationData activationData = new ActivationData();
        activationData.setHeader(header);// 设置头部信息
        activationData.setTail(tail);// 设置尾部信息

        // 正文信息
        activationData.setGatherTime(System.currentTimeMillis());
        activationData.setVin(vin);

        ProtocolEngine engine = new ProtocolEngine();
        byte[] responseBytes = GmmcDataPackUtils.addCheck(engine.encode(activationData));

        return ByteBufUtil.hexDump(responseBytes);
    }

    /**
     * 车辆登入
     * @param vin
     * @param imei
     * @param serialNumber
     * @return
     * @throws Exception
     */
    public byte[] loginDataGenerate(String vin, String imei, int serialNumber) throws Exception {
        // 包头
        Header header = getHeader(imei, LOGIN_CMD_FLAG);

        // 包尾
        Tail tail = new Tail();

        // 模拟登陆报文
        LoginData loginData = new LoginData();
        loginData.setHeader(header);// 设置头部信息
        loginData.setTail(tail);// 设置尾部信息

        // 正文信息
        loginData.setGatherTime(System.currentTimeMillis());
        loginData.setSerialNumber(serialNumber);
        byte[] aesBytes = AesUtil.generateAesSecret();
        int[] aesInts = new int[aesBytes.length];
        for (int i = 0; i < aesBytes.length; i++) {
            aesInts[i] = aesBytes[i];
        }
        loginData.setSecurityKey(aesInts);
        loginData.setSoftwareVersion("0000000000000000V1.0");
        loginData.setVin(vin);
        loginData.setElectricalCount(0);
        loginData.setElectricalLenth(0);
        loginData.setElectricalSysCode("");

        ProtocolEngine engine = new ProtocolEngine();
        byte[] data = engine.encode(loginData);

        return GmmcDataPackUtils.addCheck(data);
    }

    /**
     * 车辆登出
     * @param imei
     * @param serialNumber
     * @return
     * @throws Exception
     */
    public String logoutDataGenerate(String imei, int serialNumber) throws Exception {
        // 包头
        Header header = getHeader(imei, LOGOUT_CMD_FLAG);

        // 包尾
        Tail tail = new Tail();

        // 模拟登陆报文
        LogoutData logoutData = new LogoutData();
        logoutData.setHeader(header);// 设置头部信息
        logoutData.setTail(tail);// 设置尾部信息

        // 正文信息
        logoutData.setLogoutTime(System.currentTimeMillis());
        logoutData.setSerialNumber(serialNumber);

        ProtocolEngine engine = new ProtocolEngine();
        byte[] data = engine.encode(logoutData);

        return ByteBufUtil.hexDump(GmmcDataPackUtils.addCheck(data));
    }


    /**
     * 车辆行程
     * @param vin
     * @param imei
     * @return
     * @throws Exception
     */
    public String tripData(String vin, String imei) throws Exception {
        // 包头
        Header header = getHeader(imei, TRIP_CMD_FLAG);
        // 包尾
        Tail tail = new Tail();

        TripData tripData = new TripData();
        tripData.setHeader(header);
        tripData.setTail(tail);

        long endTime = System.currentTimeMillis();
        int speedAvg = (int)(100*(Math.random()));
        int tripDuration = (int)(100*(Math.random()));
        int mileage = (int)(speedAvg * (tripDuration/60))  ;

        tripData.setStartTime(getStartTime(endTime));
        tripData.setEndTime(endTime);
        tripData.setOilWearAvg((int)(100*(Math.random())));
        tripData.setSpeedAvg((speedAvg));
        tripData.setSpeedMax((int)(100*(Math.random())));
        tripData.setTripDuration(tripDuration);
        tripData.setMileage(mileage);
        tripData.setRapidAccelerationTimes((int)(10*(Math.random())));
        tripData.setRapidDecelerationTimes((int)(10*(Math.random())));
        tripData.setSharpTurnTimes((int)(10*(Math.random())));

        ProtocolEngine engine = new ProtocolEngine();
        byte[] data = engine.encode(tripData);

        return ByteBufUtil.hexDump(GmmcDataPackUtils.addCheck(data));

    }


    /**
     * 运行数据
     * @param vin
     * @param deviceId
     * @return
     * @throws Exception
     */
    public byte[] runData(String vin,String deviceId) throws Exception {
        // 包头
        Header header = getHeader(deviceId, RUN_CMD_FLAG);
        // 包尾
        Tail tail = new Tail();

        String OVERVIEW = "03" ;
        String GPRS = "07" ;

        RunData runData = new RunData() ;
        runData.setHeader(header);
        runData.setTail(tail);
        runData.setGatherTime(System.currentTimeMillis());

        String overview = OVERVIEW+overviewData(vin) ;
        String gprs = GPRS+gprsData(vin) ;

        byte[] bodyBuffer = HexStringUtil.parseBytes(overview+gprs) ;

        runData.setBodyBuffer(GmmcDataPackUtils.coverToIntArray(bodyBuffer));

        int length = bodyBuffer.length;

        runData.getHeader().setLength(length+6);

        ProtocolEngine engine = new ProtocolEngine();

        return engine.encode(runData);
    }

    /**
     * 定位数据
     * 0x02 --> 0x07
     * @param vin
     * @return
     * @throws Exception
     */
    public String gprsData(String vin) throws Exception {
        PositionData positionData = new PositionData() ;

        /**
         * 0:有效定位；1:无效定位（当数据通信正常，而不能获取定位信息时，发送最后一次有效定位信息，并将定位状态置为无效。）
         */
        int isValidate = 0;
        /**
         * 0:北纬；1:南纬
         */
        int latType = 0;
        /**
         * 0:东经；1:西经
         */
        int lngType = 0;
        /**
         * 保留
         */
        int reserveBit = 0 ;
        /**
         * 经度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度
         */
        double longitude = getRandom(0,0xFFFFFF) * 0.0001d;
        /**
         * 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度
         */
        double latitude = getRandom(0,0xFFFFFF) * 0.0001d;
        /**
         * 速度 有效值范围：0～2200（表示0km/h～220
         * km/h），最小计量单元：0.1km/h。“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效。
         */
        float speed = getRandom(0, 0xFFFF) * 0.1f;
        /**
         * 海拔 以米为单位，最小计量单元：0.1m
         */
        double altitude = getRandom(0,0xFFFFFF) * 0.1d;
        /**
         * 方向 有效值范围：0～359，正北为0，顺时针
         */
        int direction = getRandom(0, 359);
        /**
         * 保留位
         */
        long reserve = 0xFFFFFFFF;

        positionData.setIsValidate(isValidate);
        positionData.setLatType(latType);
        positionData.setLngType(lngType);
        positionData.setReserveBit(reserveBit);
        positionData.setLongitude(longitude);
        positionData.setLatitude(latitude);
        positionData.setSpeed(speed);
        positionData.setAltitude(altitude);
        positionData.setDirection(direction);
        positionData.setReserve(reserve);

        ProtocolEngine engine = new ProtocolEngine();
        byte[] data = engine.encode(positionData);

        return ByteBufUtil.hexDump(data);
    }

    /**
     * 车辆整车
     * CMD 0x02 -> 0x03
     * @param vin
     * @return
     */
    public String overviewData(String vin) throws Exception {

        String CARSTATUS = vin + "carStatus" ;
        putMap(CARSTATUS,CAR_STATUS);

        int carStatus = getStatus(map.get(CARSTATUS),CAR_STATUS) ;

        int chargeStatus = 0xFF;

        int runStatus = 0x03;

        float vehicleSpeed = getRandom(0,2200) * 0.1f ;

        /**
         * 累计行驶里程(km)
         */
        double mileage = 0x00101010;

        /**
         * 电压（V）
         */
        float voltage = getRandom(0,0xFFFF) * 0.1f ;

        /**
         * 电流（A）
         */
        float current = getRandom(0,0xFFFF) * 0.1f ;;

        /**
         * SOC 有效值范围： 0～100（表示 0%～100%）<br>
         */
        int soc = 0x50;
        /**
         * DC-DC 状态 0x01： 工作<br>
         */
        int dcdcStatus = 0x01;
        /**
         * <i>档位 bit0-bit3</i><br>
         */
        int clutchStatus = 0001;

        /**
         * 绝缘电阻<br>
         */
        int issueValue = getRandom(0, 60000);

        /**
         * 燃油量<br>
         */
        int fuelQuantity = getRandom(0, 250);

        /**
         * 累计平均油耗<br>
         */
        float avgOilUsed = getRandom(0, 100) * 0.1f;

        /**
         * 左前轮胎压<br>
         */
        int leftFrontTirePressure = getRandom(0, 250);

        /**
         * 右前轮胎压<br>
         */
        int rightFrontTirePressure = getRandom(0, 250);

        /**
         * 左后轮胎压<br>
         */
        int leftRearTirePressure = getRandom(0, 250);

        /**
         * 右后轮胎压<br>
         */
        int rightRearTirePressure = getRandom(0, 250);
        /**
         * <i>左前门状态<i><br>
         */
        int leftFrontDoorStatus = 0x02;

        /**
         * 左后门状态
         */
        int leftBackDoorStatus = 0x02;

        /**
         * 右前门状态
         */
        int rightFrontDoorStatus = 0x02;

        /**
         * 右后门状态
         */
        int rightBackDoorStatus = 0x02 ;

        /**
         * 前盖状态
         */
        int frontCover = 0x02;

        /**
         * 后备箱状态
         */
        int trunkStatus = 0x02;

        /**
         * 左前窗状态
         */
        int leftFrontWindowStatus = 0x02;

        /**
         * 右前窗状态
         */
        int rightFrontWindowStatus = 0x02;

        /**
         * 左后窗状态
         */
        int leftBackWindowStatus = 0x02;

        /**
         * 右后窗状态
         */
        int rightBackWindowStatus = 0x02;

        /**
         * 车外温度<br>
         */
        int outsideTemperature = getRandom(0, 250);

        /**
         * 车内温度<br>
         */
        int insideTemperature = getRandom(-40, 210);

        /**
         * 续航里程 有效值范围： 0～20000<br>
         */
        int rechargeMileage = getRandom(0, 20000);

        /**
         * 车灯状态<br>
         */
        int lightStatus = 0x02;

        /**
         * 预留字段
         */
        int reserve = getRandom(0, 0xFFFF);

        OverviewData overviewData = new OverviewData() ;

        overviewData.setCarStatus(carStatus);
        overviewData.setChargeStatus(chargeStatus);
        overviewData.setRunStatus(runStatus);
        overviewData.setVehicleSpeed(vehicleSpeed);
        overviewData.setMileage(mileage);
        overviewData.setVoltage(voltage);
        overviewData.setCurrent(current);
        overviewData.setSoc(soc);
        overviewData.setDcdcStatus(dcdcStatus);
        overviewData.setClutchStatus(clutchStatus);
        overviewData.setIssueValue(issueValue);
        overviewData.setFuelQuantity(fuelQuantity);
        overviewData.setAvgOilUsed(avgOilUsed);
        overviewData.setLeftFrontTirePressure(leftFrontTirePressure);
        overviewData.setRightFrontTirePressure(rightFrontTirePressure);
        overviewData.setLeftRearTirePressure(leftRearTirePressure);
        overviewData.setRightRearTirePressure(rightRearTirePressure);
        overviewData.setLeftFrontDoorStatus(leftFrontDoorStatus);
        overviewData.setLeftBackDoorStatus(leftBackDoorStatus);
        overviewData.setRightFrontDoorStatus(rightFrontDoorStatus);
        overviewData.setRightBackDoorStatus(rightBackDoorStatus);
        overviewData.setFrontCover(frontCover);
        overviewData.setTrunkStatus(trunkStatus);
        overviewData.setLeftFrontWindowStatus(leftFrontWindowStatus);
        overviewData.setRightFrontWindowStatus(rightFrontWindowStatus);
        overviewData.setLeftBackWindowStatus(leftBackWindowStatus);
        overviewData.setRightBackWindowStatus(rightBackWindowStatus);
        overviewData.setOutsideTemperature(outsideTemperature);
        overviewData.setInsideTemperature(insideTemperature);
        overviewData.setRechargeMileage(rechargeMileage);
        overviewData.setLightStatus(lightStatus);
        overviewData.setReserve(reserve);

        ProtocolEngine engine = new ProtocolEngine();
        byte[] data = engine.encode(overviewData);

        return ByteBufUtil.hexDump(data);
    }


    /**
     * 包头
     * @param imei
     * @param cmdFlag
     * @return
     */
    private Header getHeader(String imei, int cmdFlag){
        Header header = new Header();
        header.setCarFlag(0x01);// 车辆类型标识 0x01燃油车 ；0x02 新能源车
        header.setCarType(0x01);// 车型 0x01 ZC；0xRE NS；0x03 NS
        header.setCmdFlag(cmdFlag);// 命令标识
        header.setResponeFlag(0xFE);// 应答标识 命令包
        header.setImei(imei);// imei
        header.setEncryptType(0x00);// 加密方式
        header.setLength(0x00);// 数据单元长度
        return header;
    }


    private long getStartTime(long endTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endTime);
        calendar.add(Calendar.HOUR, (int)(-10*(Math.random())));
        return  calendar.getTimeInMillis();
    }

    /**
     * 根据下标获取状态
     * @param index
     * @param data
     * @return
     */
    private int getStatus(int index,int[] data){
        return data[index];
    }


    /**
     * <i>车辆状态</i><br>
     * <ul>
     * <li>0x01-发动机点火时</li>
     * <li>0x02-发动机运行中</li>
     * <li>0x03-发动机熄火时</li>
     * <li>0x04-发动机熄火后</li>
     * <li>0x05-车辆不能检测</li>
     * </ul>
     */
    private static int[] CAR_STATUS = new int[]{0x01, 0x02, 0x03, 0x04, 0x05};

    private ConcurrentMap<String,Integer> map = new ConcurrentHashMap<>() ;

    /**
     * 累加+一个循环
     * @param key
     * @param datas
     */
    private void putMap(String key,int[] datas){
        Integer num = map.get(key) ;
        int length = datas.length ;
        if (null == null) map.put(key,0) ;
        else map.put(key,num%length + 1) ;
    }

    /**
     * 获取随机数
     * @param start
     * @param end
     * @return
     */
    public int getRandom(int start, int end){
        Random random = new Random();
        return random.nextInt(end-start) + start ;
    }

    /**
     * Rsa 加密消息体
     * @param bytes 完整报文
     * @param publicKeyModulusString
     * @param publicKeyExponent
     * @return
     */
    public byte[] encryptRsa(byte[] bytes, String publicKeyModulusString, long publicKeyExponent) throws Exception {

        // 获取协议头
        byte[] header = new byte[24];
        System.arraycopy(bytes,0,header,0,header.length);

        //RSA加密
        header[21] = 0x01 ;

        //获取消息体数据单元
        byte[] body = new byte[bytes.length-25] ;
        System.arraycopy(bytes,24,body,0,body.length);

        //加密消息体
        byte[] securityBody = RsaUtil.encryptByRsaPublic(body,publicKeyModulusString,publicKeyExponent) ;

        byte[] dataBytes = new byte[securityBody.length + 25] ;
        System.arraycopy(header,0,dataBytes,0,24);
        System.arraycopy(securityBody,0,dataBytes,24,securityBody.length);

        //数据单元长度+检验码处理
        GmmcDataPackUtils.addCheck(dataBytes);
        return dataBytes ;
    }

    /**
     * AES 加密消息体
     * @param bytes 完整报文
     * @param securityKey
     * @return
     */
    public byte[] encryptAes(byte[] bytes,byte[] securityKey) throws Exception {

        // 获取协议头
        byte[] header = new byte[24];
        System.arraycopy(bytes,0,header,0,header.length);

        //AES加密
        header[21] = 0x02 ;

        //获取消息体数据单元
        byte[] body = new byte[bytes.length-25] ;
        System.arraycopy(bytes,24,body,0,body.length);

        //加密消息体
        byte[] securityBody = AesUtil.encrypt(body,securityKey) ;

        byte[] dataBytes = new byte[securityBody.length + 25] ;
        System.arraycopy(header,0,dataBytes,0,24);
        System.arraycopy(securityBody,0,dataBytes,24,securityBody.length);

        //数据单元长度+检验码处理
        GmmcDataPackUtils.addCheck(dataBytes);
        return dataBytes ;
    }
}
