package com.incarcloud.rooster.datapack.strategy.impl;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.DataPackTrip;
import com.incarcloud.rooster.datapack.model.TripData;
import com.incarcloud.rooster.datapack.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @Title: TripDataStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: 车辆行程信息解析策略
 * @author: chenz
 * @date: 2017年11月29日 下午12:06:14
 * @version: V1.0
 */
public class TripDataStrategy implements IDataPackStrategy {

	@Override
	public List<DataPackTarget> decode(DataPack dataPack) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
		// 车辆行程数据
		TripData tripData = null;
		try {
			tripData = engine.decode(dataBytes, TripData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回的解析数据
		List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
		// 基础信息
		DataPackObject dataPackObject = new DataPackObject(dataPack);

		// 设备ID
		dataPackObject.setDeviceId(tripData.getHeader().getImei());
		// 设置数据采集时间
		dataPackObject.setDetectionTime(new Date(tripData.getStartTime()));
		// 设置数据接收时间
		dataPackObject.setReceiveTime(new Date());
		// 设置数据加密方式
		dataPackObject.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(tripData.getHeader().getEncryptType()));

		// 构建返回的数据
		DataPackTrip dataPackTrip = new DataPackTrip(dataPackObject);
		dataPackTrip.setStartTime(new Date(tripData.getStartTime()));// 行程开始时间
		dataPackTrip.setEndTime(new Date(tripData.getEndTime()));// 行程结束时间
		dataPackTrip.setOilWearAvg(tripData.getOilWearAvg());// 平均油耗
		dataPackTrip.setSpeedAvg(tripData.getSpeedAvg());// 平均车速
		dataPackTrip.setSpeedMax(tripData.getSpeedMax());// 最高车速
		dataPackTrip.setTripDuration(tripData.getTripDuration());// 行程时长
		dataPackTrip.setMileage((int) (tripData.getMileage() * 1000));// 行程行驶里程,返回给后台的数据转换为米
		dataPackTrip.setRapidAccelerationTimes(tripData.getRapidAccelerationTimes());// 急加速次数
		dataPackTrip.setRapidDecelerationTimes(tripData.getRapidDecelerationTimes());// 急减速次数
		dataPackTrip.setSharpTurnTimes(tripData.getSharpTurnTimes());// 急转弯次数

		// 添加返回结果集
		dataPackTargetList.add(new DataPackTarget(dataPackTrip));

		return dataPackTargetList;
	}

	@Override
	public byte[] encode(DataPack dataPack) {
		return null;
	}

}
