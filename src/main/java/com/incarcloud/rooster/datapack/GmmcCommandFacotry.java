package com.incarcloud.rooster.datapack;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.model.Header;
import com.incarcloud.rooster.datapack.model.OtaUpdateData;
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

	@Override
	public ByteBuf createCommand(CommandType type, Object... args) throws Exception {
		// 解析器
		ProtocolEngine engie = new ProtocolEngine();
		byte[] responseBytes = {};
		// 基本验证，必须有参数，第一个为设备号deviceId
		String deviceId = (String) args[0];

		if (null == args && 0 < args.length) {
			throw new IllegalArgumentException("args is null");
		}

		/**
		 * deviceCode不足15位，抛出异常。
		 */
		if (deviceId.getBytes().length < 15) {
			throw new IllegalArgumentException("device code少于15位!");
		}

		// 根据type生成控制指令
		switch (type) {
			case OPEN_DOOR: // 打开车门

				break;
			case CLOSE_DOOR: // 关闭车门

				break;
			case BACK_DOOR_UNLOCK: // 远程后备箱解锁

				break;
			case FIND_CAR: // 远程寻车

				break;
			case LEFT_WIN_UP: // 左前车窗控制-上升

				break;
			case LEFT_WIN_DOWN: // 左前车窗控制-下降

				break;
			case RIGHT_WIN_UP: // 右前车窗控制-上升

				break;
			case RIGHT_WIN_DOWN: // 右前车窗控制-下降

				break;
			case COND_HEAT_OPEN: // 空调系统制热控制-开启

				break;
			case COND_HEAT_CLOSE: // 空调系统制热控制-关闭

				break;
			case COND_COLD_OPEN: // 空调系统制冷控制-开启

				break;
			case COND_COLD_CLOSE: // 空调系统制冷控制-关闭

				break;
			case VEHICLE_POWER_ON: // 车辆动力通断控制-动力导通

				break;
			case VEHICLE_POWER_OFF: // 车辆动力通断控制-动力断开

				break;
			case LITTLE_LIGHT_ON: // 小灯控制-开

				break;
			case LITTLE_LIGHT_OFF: // 小灯控制-关

				break;
			case TBOX_WAKE_UP: // 车机唤醒

				break;
			case TBOX_POWER_OFF: // 车机关机

				break;
			case TBOX_UPDATE: // 终端升级

				break;
			case ALARM_PARAM_QUERY: // 报警参数查询

				break;
			case ALARM_PARAM_SET: // 报警参数设置

				break;
			case GET_RUN_INFO: // 获取车辆运行数据

				break;

			/**
			 * 传递四个参数<br>
			 * 1> deviceId 设备号<br>
			 * 2> softwareVersion 当前软件版本<br>
			 * 3> updatePackageName 升级包文件名<br>
			 * 4> url 下载链接URL<br>
			 */
			case OTA: // ota升级

				if (args.length == 4) {
					OtaUpdateData ota = new OtaUpdateData();

					String softwareVersion = (String) args[1];
					String updatePackageName = (String) args[2];
					String url = (String) args[3];
					// 头部信息
					Header header = new Header();
					header.setCarFlag(0x01);// 车辆类型标识 0x01燃油车 ；0x02 新能源车
					header.setCarType(0x01);// 车型 0x01 ZC；0xRE NS；0x03 NS
					header.setCmdFlag(0x23);// 命令标识
					header.setResponeFlag(0xFE);// 应答标识 命令包
					header.setImei(deviceId);// imei
					header.setEncryptType(0x01);// 加密方式
					header.setLength(0x00);// 数据单元长度

					ota.setHeader(header);

					// 数据单元
					ota.setGatherTime(System.currentTimeMillis()); // 发送时间
					ota.setSoftwareVersionLength(softwareVersion.getBytes().length);// 软件版本长度
					ota.setSoftwareVersion(softwareVersion);// 软件版本
					ota.setUpdatePackageNameLength(updatePackageName.getBytes().length);// 包名长度
					ota.setUpdatePackageName(updatePackageName);// 包名
					ota.setUrlLength(url.getBytes().length);// url长度
					ota.setUrl(url);// url
					ota.setTail(new Tail());// 包尾

					responseBytes = engie.encode(ota);
					responseBytes = GmmcDataPackUtils.addCheck(responseBytes);
					System.out.println(HexStringUtil.toHexString(responseBytes));
				}

				break;

			case SET_PARAMS: // 参数设置

				break;
		}
		/* ====================end---判断msgId回复消息---end==================== */

		// 添加包体长度和校验码
		responseBytes = GmmcDataPackUtils.addCheck(responseBytes);
		// 打印调试信息
		GmmcDataPackUtils.debug(ByteBufUtil.hexDump(responseBytes));

		// return
		return Unpooled.wrappedBuffer(responseBytes);
	}

	public static void main(String[] args) throws Exception {
		GmmcCommandFacotry cmd = new GmmcCommandFacotry();
		// cmd.createCommand(CommandType.CLOSE_DOOR, 0x01);
		cmd.createCommand(CommandType.COND_COLD_CLOSE, "862234021042470", 1000, 28);
	}
}
