package com.incarcloud.rooster.datapack;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.model.CommonRespData;
import com.incarcloud.rooster.datapack.model.DownlinkControlAirData;
import com.incarcloud.rooster.datapack.model.DownlinkControlAirMoreData;
import com.incarcloud.rooster.datapack.model.DownlinkControlData;
import com.incarcloud.rooster.datapack.model.Header;
import com.incarcloud.rooster.datapack.model.OtaUpdateData;
import com.incarcloud.rooster.datapack.model.ParameterSettingData;
import com.incarcloud.rooster.datapack.model.Tail;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;
import com.incarcloud.rooster.gather.cmd.CommandFacotryManager;
import com.incarcloud.rooster.gather.cmd.CommandFactory;
import com.incarcloud.rooster.gather.cmd.CommandType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * GMMC设备协议命令工厂
 *
 * @author Aaric, created on 2017-11-22T17:54.
 * @since 2.0
 */
public class GmmcCommandFacotry implements CommandFactory {

	static {
		/**
		 * 声明数据包版本与命令工厂类关系
		 */
		CommandFacotryManager.registerCommandFacotry(DataParserGmmc.PROTOCOL_PREFIX + "0.3", GmmcCommandFacotry.class);
	}

	/**
	 * 必传参数<br>
	 * 1> deviceId 设备ID args[0]<br>
	 * 2> serialNumber 流水号 args[1]<br>
	 * 3> publicKey 公钥 args[2] <br>
	 */
	@Override
	public ByteBuf createCommand(CommandType type, Object... args) throws Exception {

		if (null == args && 0 < args.length) {
			throw new IllegalArgumentException("args is null");
		}

		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		byte[] responseBytes = {};
		// 基本验证，必须有参数，第一个为设备号deviceId
		String deviceId = (String) args[0]; // 设备号-imei
		int serialNumber = (int) args[1];// 流水号
		String publicKey = null; // 公钥

		/**
		 * deviceCode不足15位，抛出异常。
		 */
		if (deviceId.getBytes().length < 15) {
			throw new IllegalArgumentException("device id少于15位!");
		}

		// 下行控制指令
		DownlinkControlData downlink = new DownlinkControlData();
		// 空调系统制冷/制热控制
		DownlinkControlAirData downlinkAir = new DownlinkControlAirData();
		// 空调系统自动模式控制
		DownlinkControlAirMoreData downlinkAirMore = new DownlinkControlAirMoreData();
		// 报文头部信息
		Header header = new Header();
		Tail tail = new Tail();

		/**
		 * publickey为null：报文不加密<br>
		 * publickey不为null：报文加密<br>
		 */
		if (null == args[2]) {
			header.setEncryptType(0x00);// 加密方式-不加密
		} else {
			header.setEncryptType(0x01);// 加密方式-加密
			publicKey = (String) args[2];// 公钥
		}

		// 下行控制默认报文体
		header.setCarFlag(0x01);// 车辆类型标识 0x01燃油车 ；0x02 新能源车
		header.setCarType(0x01);// 车型 0x01 ZC；0xRE NS；0x03 NS
		header.setCmdFlag(0x83);// 命令标识-车辆控制命令
		header.setResponeFlag(0xFE);// 应答标识 命令包
		header.setImei(deviceId);// imei
		header.setLength(0x00);// 数据单元长度
		downlink.setHeader(header);// 设置头部信息
		downlink.setTail(tail);// 设置尾部信息

		// 单参数指令下发
		downlink.setGatherTime(System.currentTimeMillis());// 报文时间
		downlink.setSerialNumber(serialNumber);// 流水号
		downlink.setCmdNumber(1);// 指令数量-目前只支持单条指令下发

		// 空调系统制冷/制热控制
		downlinkAir.setGatherTime(System.currentTimeMillis());// 报文时间
		downlinkAir.setSerialNumber(serialNumber);// 流水号
		downlinkAir.setCmdNumber(1);// 指令数量-目前只支持单条指令下发
		downlinkAir.setHeader(header);
		downlinkAir.setTail(tail);

		// 空调系统自动模式控制
		downlinkAirMore.setGatherTime(System.currentTimeMillis());// 报文时间
		downlinkAirMore.setSerialNumber(serialNumber);// 流水号
		downlinkAirMore.setCmdNumber(1);// 指令数量-目前只支持单条指令下发
		downlinkAirMore.setHeader(header);
		downlinkAirMore.setTail(tail);

		// 根据type生成控制指令
		switch (type) {
			case OPEN_DOOR: // 打开车门
				downlink.setCmdId(0x01);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(1);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case CLOSE_DOOR: // 关闭车门
				downlink.setCmdId(0x01);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(2);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case BACK_DOOR_UNLOCK: // 远程后备箱解锁
				downlink.setCmdId(0x02);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(1);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case FIND_CAR: // 远程寻车
				downlink.setCmdId(0x03);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(1);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case LEFT_WIN_UP: // 左前车窗控制-上升
				downlink.setCmdId(0x04);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(1);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case LEFT_WIN_DOWN: // 左前车窗控制-下降
				downlink.setCmdId(0x04);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(2);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case RIGHT_WIN_UP: // 右前车窗控制-上升
				downlink.setCmdId(0x05);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(1);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case RIGHT_WIN_DOWN: // 右前车窗控制-下降
				downlink.setCmdId(0x05);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(2);// 指令参数
				responseBytes = engine.encode(downlink);
				break;

			case COND_HEAT_OPEN: // 空调系统制热控制-开启 args[3] args[4]
				if (args.length == 5) {
					int time = (int) args[3];// 时间
					int temp = (int) args[4];// 温度
					downlinkAir.setCmdId(0x06);// 控制指令ID
					downlinkAir.setCmdMode(1);// 空调制热控制1：开启 2：关闭
					downlinkAir.setCmdLength(4);// 指令长度
					downlinkAir.setTimeSetting(time);// 时间设定
					downlinkAir.setTemSetting(temp);// 温度设定
					responseBytes = engine.encode(downlinkAir);
				}

				break;
			case COND_HEAT_CLOSE: // 空调系统制热控制-关闭
				if (args.length == 5) {
					int time = (int) args[3];// 时间
					int temp = (int) args[4];// 温度

					downlinkAir.setCmdId(0x06);// 控制指令ID
					downlinkAir.setCmdMode(2);// 空调制热控制1：开启 2：关闭
					downlinkAir.setCmdLength(4);// 指令长度
					downlinkAir.setTimeSetting(time);// 时间设定
					downlinkAir.setTemSetting(temp);// 温度设定
					responseBytes = engine.encode(downlinkAir);
				}
				break;
			case COND_COLD_OPEN: // 空调系统制冷控制-开启
				if (args.length == 5) {
					int time = (int) args[3];// 时间
					int temp = (int) args[4];// 温度

					downlinkAir.setCmdId(0x07);// 控制指令ID
					downlinkAir.setCmdMode(1);// 空调制热控制1：开启 2：关闭
					downlinkAir.setCmdLength(4);// 指令长度
					downlinkAir.setTimeSetting(time);// 时间设定
					downlinkAir.setTemSetting(temp);// 温度设定
					responseBytes = engine.encode(downlinkAir);
				}
				break;
			case COND_COLD_CLOSE: // 空调系统制冷控制-关闭
				if (args.length == 5) {
					int time = (int) args[3];// 时间
					int temp = (int) args[4];// 温度

					downlinkAir.setCmdId(0x07);// 控制指令ID
					downlinkAir.setCmdMode(2);// 空调制热控制1：开启 2：关闭
					downlinkAir.setCmdLength(4);// 指令长度
					downlinkAir.setTimeSetting(time);// 时间设定
					downlinkAir.setTemSetting(temp);// 温度设定
					responseBytes = engine.encode(downlinkAir);
				}
			case VEHICLE_POWER_ON: // 车辆动力通断控制-动力导通
				downlink.setCmdId(0x08);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(1);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case VEHICLE_POWER_OFF: // 车辆动力通断控制-动力断开
				downlink.setCmdId(0x08);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(0);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case LITTLE_LIGHT_ON: // 小灯控制-开
				downlink.setCmdId(0x09);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(1);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case LITTLE_LIGHT_OFF: // 小灯控制-关
				downlink.setCmdId(0x09);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(2);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case TBOX_WAKE_UP: // 车机唤醒
				downlink.setCmdId(0x12);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(1);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case TBOX_POWER_OFF: // 车机关机
				downlink.setCmdId(0x12);// 命令ID
				downlink.setCmdLength(1);// 指令长度
				downlink.setCmdContent(0);// 指令参数
				responseBytes = engine.encode(downlink);
				break;
			case TBOX_UPDATE: // 终端升级

				break;
			case ALARM_PARAM_QUERY: // 报警参数查询

				break;
			case ALARM_PARAM_SET: // 报警参数设置

				break;
			case GET_RUN_INFO: // 获取车辆运行数据
				CommonRespData getRunInfo = new CommonRespData();
				header.setCmdFlag(0x86);
				getRunInfo.setHeader(header);
				getRunInfo.setTail(tail);

				responseBytes = engine.encode(getRunInfo);
				break;

			/**
			 * 传递四个参数<br>
			 * 1> deviceId 设备号<br>
			 * 2> softwareVersion 当前软件版本<br>
			 * 3> updatePackageName 升级包文件名<br>
			 * 4> url 下载链接URL<br>
			 */
			case OTA: // ota升级
				if (args.length == 6) {
					OtaUpdateData ota = new OtaUpdateData();

					String softwareVersion = (String) args[3];
					String updatePackageName = (String) args[4];
					String url = (String) args[5];
					// 头部信息
					// Header header = new Header();
					// header.setCarFlag(0x01);// 车辆类型标识 0x01燃油车 ；0x02 新能源车
					// header.setCarType(0x01);// 车型 0x01 ZC；0xRE NS；0x03 NS
					// header.setCmdFlag(0x23);// 命令标识
					// header.setResponeFlag(0xFE);// 应答标识 命令包
					// header.setImei(deviceId);// imei
					// header.setEncryptType(0x01);// 加密方式
					// header.setLength(0x00);// 数据单元长度

					header.setCmdFlag(0x23);// 设置命令ID ota升级指令
					ota.setHeader(header);

					// 数据单元
					ota.setGatherTime(System.currentTimeMillis()); // 发送时间
					ota.setSoftwareVersionLength(softwareVersion.getBytes().length);// 软件版本长度
					ota.setSoftwareVersion(softwareVersion);// 软件版本
					ota.setUpdatePackageNameLength(updatePackageName.getBytes().length);// 包名长度
					ota.setUpdatePackageName(updatePackageName);// 包名
					ota.setUrlLength(url.getBytes().length);// url长度
					ota.setUrl(url);// url
					ota.setTail(tail);// 包尾

					responseBytes = engine.encode(ota);// 报文封包
				}

				break;

			case SET_PARAMS: // 参数设置
				if (args.length == 33) {
					ParameterSettingData setting = new ParameterSettingData();
					header.setCmdFlag(0x21);// 参数设置命令
					setting.setHeader(header);
					setting.setGatherTime(System.currentTimeMillis());
					/**
					 * 参数版本 0～60000 整型
					 */
					int version = (int) args[3];
					setting.setVersion(version);
					/**
					 * 车载终端本地存储时间周期
					 */
					int tboxSaveCycle = (int) args[4];
					setting.setTboxSaveCycle(tboxSaveCycle);
					/**
					 * 正常时，CAN信息上报时间周期
					 */
					int canUpDataCycle = (int) args[5];
					setting.setCanUpDataCycle(canUpDataCycle);
					/**
					 * 出现报警时，CAN信息上报时间周期
					 */
					int canErrorDataCycle = (int) args[6];
					setting.setCanErrorDataCycle(canErrorDataCycle);
					/**
					 * 远程服务与管理平台域名或ip地址长度m
					 */
					int ipLength = (int) args[7];
					setting.setIpLength(ipLength);
					/**
					 * 远程服务与管理平台域名或ip地址
					 */
					String ip = (String) args[8];
					setting.setIp(ip);
					/**
					 * 远程服务与管理平台端口
					 */
					int port = (int) args[9];
					setting.setPort(port);
					/**
					 * 车载终端心跳发送周期
					 */
					int heartBeatCycle = (int) args[10];
					setting.setHeartBeatCycle(heartBeatCycle);
					/**
					 * 终端应答超时时间
					 */
					int tboxResp = (int) args[11];
					setting.setTboxResp(tboxResp);
					/**
					 * 平台应答超时时间
					 */
					int platTimeout = (int) args[12];
					setting.setPlatTimeout(platTimeout);
					/**
					 * 连续三次登入失败后，到下一次登入的间隔时间
					 */
					int threeErrPeriod = (int) args[13];
					setting.setThreeErrPeriod(threeErrPeriod);
					/**
					 * 政府平台域名长度p
					 */
					int govHostnameLength = (int) args[14];
					setting.setGovHostnameLength(govHostnameLength);
					/**
					 * 政府平台域名
					 */
					String govHostname = (String) args[15];
					setting.setGovHostname(govHostname);
					/**
					 * 政府平台端口
					 */
					int govPort = (int) args[16];
					setting.setGovPort(govPort);
					/**
					 * 否处于抽样监测中“0x01”表示是，“0x02”表示否，“0xFE”表示异常，“0xFF”表示无效
					 */
					int sample = (int) args[17];
					setting.setSample(sample);
					/**
					 * 延时工作模式下，心跳发送周期
					 */
					int canDelay = (int) args[18];
					setting.setCanDelay(canDelay);
					/**
					 * 休眠时，定位信息上报时间周期
					 */
					int heartbeatDelay = (int) args[19];
					setting.setHeartbeatDelay(heartbeatDelay);
					/**
					 * 休眠时，定位信息上报时间周期
					 */
					int sleepUpDataCycle = (int) args[20];
					setting.setSleepUpDataCycle(sleepUpDataCycle);

					/**
					 * 充电时，CAN信息上报时间周期
					 */
					int chargeUpDataCycle = (int) args[21];
					setting.setChargeUpDataCycle(chargeUpDataCycle);
					/**
					 * 延时时间
					 */
					int delay = (int) args[22];
					setting.setDelay(delay);
					/**
					 * 正常时，定位信息上报时间周期，
					 */
					int gpsUpCycle = (int) args[23];
					setting.setGpsUpCycle(gpsUpCycle);
					/**
					 * 出现报警时，定位信息上报时间周期
					 */
					int alarmGpsCycle = (int) args[24];
					setting.setAlarmGpsCycle(alarmGpsCycle);
					/**
					 * 延时工作模式下，定位信息上报时间周期
					 */
					int delayGpsCycle = (int) args[25];
					setting.setDelayGpsCycle(delayGpsCycle);
					/**
					 * 充电时，定位信息上报时间周期
					 */
					int chargeGpsCycle = (int) args[26];
					setting.setChargeGpsCycle(chargeGpsCycle);
					/**
					 * 超速报警阀值
					 */
					int overspeed = (int) args[27];
					setting.setOverspeed(overspeed);
					/**
					 * 租赁平台域名或IP长度
					 */
					int rentIplength = (int) args[28];
					setting.setRentIplength(rentIplength);
					/**
					 * 租赁平台域名或IP
					 */
					String rentIp = (String) args[29];
					setting.setRentIp(rentIp);
					/**
					 * 租赁平台端口
					 */
					int rentPort = (int) args[30];
					setting.setRentPort(rentPort);
					/**
					 * APN，1移动CMNET，2联通UNINET 3 电信
					 */
					int apn = (int) args[31];
					setting.setApn(apn);
					/**
					 * PublicKey更新周期
					 */
					int publicKeyUpdateCycle = (int) args[32];
					setting.setPublicKeyUpdateCycle(publicKeyUpdateCycle);
					setting.setTail(tail);// 尾部信息

					responseBytes = engine.encode(setting);// 报文封包

				}

				break;

			default:
				// 打印调试信息
				GmmcDataPackUtils.debug(type.toString() + "无此下发指令");
				break;
		}
		/* ====================end---判断msgId回复消息---end==================== */

		// 添加包体长度和校验码
		responseBytes = GmmcDataPackUtils.addCheck(responseBytes);
		// 打印调试信息
		GmmcDataPackUtils.debug(HexStringUtil.toHexString(responseBytes));
		// return
		return Unpooled.wrappedBuffer(responseBytes);
	}
}
