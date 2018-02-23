package com.incarcloud.rooster.datapack.gmmc.strategy.impl;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.*;
import com.incarcloud.rooster.datapack.gmmc.model.AlarmData;
import com.incarcloud.rooster.datapack.gmmc.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @Title: AlarmDataStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: 车辆告警信息解析策略
 * @author: chenz
 * @date: 2017年11月30日 上午10:12:47
 * @version: V1.0
 */
public class AlarmDataStrategy implements IDataPackStrategy {
	/**
	 * 安全气囊告警<br>
	 * 0:未触发,1:触发
	 */
	public static final String ARI_BAG_ALARM = "COM_ariBagAlarm";
	/**
	 * 碰撞告警<br>
	 * 0:未触发,1:触发
	 */
	public static final String CRASH_ALARM = "COM_crashAlarm";
	/**
	 * 防盗告警<br>
	 * 0:未触发,1:触发
	 */
	public static final String ANTITHEFT_ALARM = "COM_antiTheftAlarm";
	/**
	 * 灯光未关闭告警<br>
	 * 0:未触发,1:触发
	 */
	public static final String LIGHT_NOT_CLOSED_ALARM = "COM_lightNotClosedAlarm";
	/**
	 * 蓄电池电压异常告警<br>
	 * 0:未触发,1:触发
	 */
	public static final String BATTERY_VOLTAGE_ALARM = "COM_batteryVoltageAlarm";

	/**
	 * 解析报文
	 * 
	 * @param dataPack
	 * @return
	 */
	@Override
	public List<DataPackTarget> decode(DataPack dataPack) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
		// 车辆告警信息
		AlarmData alarmData = null;
		try {
			alarmData = engine.decode(dataBytes, AlarmData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回的解析数据
		List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
		// 基础信息
		DataPackObject dataPackObject = new DataPackObject(dataPack);
		// 设备ID
		dataPackObject.setDeviceId(alarmData.getHeader().getImei());
		// 设置数据采集时间
		dataPackObject.setDetectionTime(new Date(alarmData.getGatherTime()));
		// 设置数据接收时间
		dataPackObject.setReceiveTime(new Date());
		// 设置数据加密方式
		dataPackObject.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(alarmData.getHeader().getEncryptType()));

		// 报警数据
		DataPackAlarm dataPackAlarm = new DataPackAlarm(dataPackObject);
		// 位置信息
		DataPackPosition dataPackPosition = new DataPackPosition(dataPackObject);
		// 经度
		dataPackPosition.setLongitude(alarmData.getLongitude());
		// 纬度
		dataPackPosition.setLatitude(alarmData.getLatitude());
		// 海拔
		dataPackPosition.setAltitude(alarmData.getAltitude());
		// 方向
		dataPackPosition.setDirection((float) alarmData.getDirection());
		// 定位方式
		if (0 == alarmData.getIsValidate()) {
			dataPackPosition.setPositioMode(DataPackPosition.POSITION_MODE_UNKNOWN);
		} else {
			dataPackPosition.setPositioMode(DataPackPosition.POSITION_MODE_INVALID);
		}
		dataPackAlarm.setPosition(dataPackPosition);
		// 告警信息列表
		List<DataPackAlarm.Alarm> alarmList = new ArrayList<>();
		alarmList.add(genAlarmInfo(ARI_BAG_ALARM, alarmData.getAriBagAlarm(), "安全气囊告警"));
		alarmList.add(genAlarmInfo(CRASH_ALARM, alarmData.getCrashAlarm(), "碰撞告警"));
		alarmList.add(genAlarmInfo(ANTITHEFT_ALARM, alarmData.getAntiTheftAlarm(), "防盗告警"));
		alarmList.add(genAlarmInfo(LIGHT_NOT_CLOSED_ALARM, alarmData.getLightNotClosedAlarm(), "灯光未关闭告警"));
		alarmList.add(genAlarmInfo(BATTERY_VOLTAGE_ALARM, alarmData.getBatteryVoltageAlarm(), "蓄电池电压异常告警"));
		dataPackAlarm.setAlarmList(alarmList);
		// 添加返回结果集
		dataPackTargetList.add(new DataPackTarget(dataPackAlarm));
		return dataPackTargetList;
	}

	/**
	 * 报文封包
	 * 
	 * @param dataPack
	 * @return
	 */
	@Override
	public byte[] encode(DataPack dataPack) {
		return null;
	}

	/**
	 * 生成报警信息
	 *
	 * @param alarmCode
	 *            报警编码
	 * @param alarmValue
	 *            字段值
	 * @param alarmName
	 *            报警名称
	 * @return alarmInfo
	 */
	private DataPackAlarm.Alarm genAlarmInfo(String alarmCode, Integer alarmValue, String alarmName) {
		DataPackAlarm.Alarm alarmInfo = new DataPackAlarm.Alarm();
		alarmInfo.setAlarmCode(alarmCode);
		alarmInfo.setAlarmName(alarmName);
		alarmInfo.setAlarmValue(alarmValue.toString());
		return alarmInfo;
	}

}
