package com.incarcloud.rooster.datapack.gmmc.strategy.impl;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.*;
import com.incarcloud.rooster.datapack.gmmc.model.OverviewData;
import com.incarcloud.rooster.datapack.gmmc.model.PositionData;
import com.incarcloud.rooster.datapack.gmmc.model.RunData;
import com.incarcloud.rooster.datapack.gmmc.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @Title: RunDataStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: TODO 车辆运行数据解析策略
 * @author: chenz
 * @date: 2017年11月30日 下午4:20:54
 * @version: V1.0
 */
public class RunDataStrategy implements IDataPackStrategy {
	/**
	 * 报文解析
	 */
	@Override
	public List<DataPackTarget> decode(DataPack dataPack) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
		// 车辆行程数据
		RunData runData = null;
		try {
			runData = engine.decode(dataBytes, RunData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回的解析数据
		List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
		// 基础信息
		DataPackObject dataPackObject = new DataPackObject(dataPack);

		// 设备ID
		dataPackObject.setDeviceId(runData.getHeader().getImei());
		// 设置数据采集时间
		dataPackObject.setDetectionTime(new Date(runData.getGatherTime()));
		// 设置数据接收时间
		dataPackObject.setReceiveTime(new Date());
		// 设置数据加密方式
		dataPackObject.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(runData.getHeader().getEncryptType()));
		/**
		 * 解析运行数据
		 */
		int[] dataBufferOfInt = runData.getBodyBuffer();// 数据单元
		byte[] dataBuffer = GmmcDataPackUtils.coverToByteArray(dataBufferOfInt);

		if (dataBuffer != null && dataBuffer.length > 0) {
			int index = 0;// 数据单元长度计数
			while (index < dataBuffer.length) {
				if (dataBuffer[index] == (byte) 0x01) { // 动力蓄电池电气数据
					index += 1;// 增加命令ID长度
					/**
					 * 数据单元固定长度+单体蓄电池电压值(动态参数=2*电压值长度)
					 */
					int length = 11 + (dataBuffer[index + 10] & 0xFF) * 2;
					byte[] eleBuffer = new byte[length];
					System.arraycopy(dataBuffer, index, eleBuffer, 0, length);
					// TODO 解析动力蓄电池电气参数
					index = index + length;
				} else if (dataBuffer[index] == (byte) 0x02) { // 动力蓄电池包温度数据
					index += 1;
					int length = 4 + ((dataBuffer[index + 2] & 0xFF << 8) | (dataBuffer[index + 3] & 0xFF));
					byte[] eleBuffer = new byte[length];
					System.arraycopy(dataBuffer, index, eleBuffer, 0, length);
					index = index + length;
				} else if (dataBuffer[index] == (byte) 0x03) { // 整车数据
					try {
						index += 1;
						int length = 42;
						byte[] eleBuffer = new byte[length];
						System.arraycopy(dataBuffer, index, eleBuffer, 0, length);
						OverviewData overviewData = engine.decode(eleBuffer, OverviewData.class);
						// 添加返回结果集
						DataPackOverview dataPackOverview = new DataPackOverview(dataPackObject);
						dataPackOverview.setCarStatus(overviewData.getCarStatus());// 车辆状态
						dataPackOverview.setChargeStatus(overviewData.getChargeStatus());// 充电状态
						dataPackOverview.setRunStatus(overviewData.getRunStatus());// 运行模式
						dataPackOverview.setVehicleSpeed(overviewData.getVehicleSpeed());// 实时车速
						dataPackOverview.setMileage(overviewData.getMileage());// 累计行驶里程(km)
						dataPackOverview.setVoltage(overviewData.getVoltage());// 电压（V）
						dataPackOverview.setCurrent(overviewData.getCurrent());// 电流（A）
						dataPackOverview.setSoc(overviewData.getSoc());// SOC
						dataPackOverview.setDcdcStatus(overviewData.getDcdcStatus());// dcdc状态

						/**
						 * 档位状态
						 */
						int clutchStatus = overviewData.getClutchStatus();
						// 档位
						int gear = clutchStatus & 0x0f;
						dataPackOverview.setClutchStatus(gear);
						// 制动状态
						int brakeStatus = (clutchStatus >>> 4) & 0x01;
						dataPackOverview.setBrakeStatus(brakeStatus);
						// 驱动状态
						int driveStatus = (clutchStatus >>> 5) & 0x01;
						dataPackOverview.setDriveStatus(driveStatus);

						dataPackOverview.setIssueValue(overviewData.getIssueValue());// 绝缘电阻
						dataPackOverview.setFuelQuantity(overviewData.getFuelQuantity());// 燃油量
						dataPackOverview.setAvgOilUsed(overviewData.getAvgOilUsed());// 累计平均油耗
						dataPackOverview.setLeftFrontDoorStatus(overviewData.getLeftFrontDoorStatus());// 左前轮胎压
						dataPackOverview.setRightBackDoorStatus(overviewData.getRightBackDoorStatus());// 右前轮胎压
						dataPackOverview.setLeftRearTirePressure(overviewData.getLeftRearTirePressure());// 左后轮胎压
						dataPackOverview.setRightRearTirePressure(overviewData.getRightRearTirePressure());// 右后轮胎压
						dataPackOverview.setLeftFrontDoorStatus(overviewData.getLeftFrontDoorStatus());// 左前门状态
						dataPackOverview.setLeftBackDoorStatus(overviewData.getLeftBackDoorStatus());// 左后门状态
						dataPackOverview.setRightFrontDoorStatus(overviewData.getRightFrontDoorStatus());// 右前门状态
						dataPackOverview.setRightBackDoorStatus(overviewData.getRightBackDoorStatus());// 右后门状态
						dataPackOverview.setFrontCover(overviewData.getFrontCover());// 前盖状态
						dataPackOverview.setTrunkStatus(overviewData.getTrunkStatus());// 后备箱状态
						dataPackOverview.setLeftFrontWindowStatus(overviewData.getLeftFrontWindowStatus());// 左前窗状态
						dataPackOverview.setRightFrontWindowStatus(overviewData.getRightFrontWindowStatus());// 右前窗状态
						dataPackOverview.setLeftBackWindowStatus(overviewData.getLeftBackWindowStatus());// 左后窗状态
						dataPackOverview.setRightBackWindowStatus(overviewData.getRightBackWindowStatus());// 右后窗状态
						dataPackOverview.setOutsideTemperature(overviewData.getOutsideTemperature());// 车外温度
						dataPackOverview.setInsideTemperature(overviewData.getInsideTemperature());// 车内温度
						dataPackOverview.setRechargeMileage(overviewData.getRechargeMileage());// 续航里程
						dataPackOverview.setLightStatus(overviewData.getLightStatus());// 车灯状态

						dataPackTargetList.add(new DataPackTarget(dataPackOverview));

						index = index + length;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (dataBuffer[index] == (byte) 0x04) { // 汽车电机部分数据
					index += 1;
					int length = 13;
					byte[] eleBuffer = new byte[length];
					System.arraycopy(dataBuffer, index, eleBuffer, 0, length);
					index = index + length;
				} else if (dataBuffer[index] == (byte) 0x07) { // 车辆位置数据
					try {
						index += 1;
						int length = 21;
						byte[] eleBuffer = new byte[length];
						System.arraycopy(dataBuffer, index, eleBuffer, 0, length);

						// 位置数据
						PositionData positionData = engine.decode(eleBuffer, PositionData.class);
						System.out.println("===positionData==="+engine.toPrettyHexString(positionData));
						// 返回结果集
						DataPackPosition dataPackPosition = new DataPackPosition(dataPackObject);
						// 定位方式
						int isValidate = positionData.getIsValidate();
						// 定位方式
						if (0 == isValidate) {
							dataPackPosition.setPositioMode(DataPackPosition.POSITION_MODE_UNKNOWN);
						} else {
							dataPackPosition.setPositioMode(DataPackPosition.POSITION_MODE_INVALID);
						}
						// 经度
						dataPackPosition.setLongitude(positionData.getLongitude());
						// 纬度
						dataPackPosition.setLatitude(positionData.getLatitude());
						// 海拔
						dataPackPosition.setAltitude(positionData.getAltitude());
						// 方向
						dataPackPosition.setDirection((float) positionData.getDirection());

						dataPackTargetList.add(new DataPackTarget(dataPackPosition));

						index = index + length;
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (dataBuffer[index] == (byte) 0x08) { // 极值数据
					index += 1;
					int length = 14;
					byte[] eleBuffer = new byte[length];
					System.arraycopy(dataBuffer, index, eleBuffer, 0, length);

					index = index + length;

				} else if (dataBuffer[index] == (byte) 0x09) { // 透传数据
					index += 1;
					int canPacketNumber = dataBuffer[index] & 0xFF;
					int length = canPacketNumber * 12;
					index += 1;
					byte[] canAllBuffer = new byte[length];
					System.arraycopy(dataBuffer, index, canAllBuffer, 0, length);

					index = index + length;
				} else if (dataBuffer[index] == (byte) 0x0A) { // 报警数据
					index += 1;
					int length = 9;
					byte[] eleBuffer = new byte[length];
					System.arraycopy(dataBuffer, index, eleBuffer, 0, length);
					index = index + length;
				} else {
					break;
				}
			}
		}

		return dataPackTargetList;
	}

	/**
	 * 报文封包
	 */
	@Override
	public byte[] encode(DataPack dataPack) {
		return null;
	}
}
