package com.incarcloud.rooster.datapack.strategy.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackHeartbeat;
import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.model.HeartbeatData;
import com.incarcloud.rooster.datapack.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

/**
 * @Title: HeartbeatStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: 心跳信息解析策略
 * @author: chenz
 * @date: 2017年11月30日 下午3:32:48
 * @version: V1.0
 */
public class HeartbeatStrategy implements IDataPackStrategy {

	@Override
	public List<DataPackTarget> decode(DataPack dataPack) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
		// 车辆告警信息
		HeartbeatData heartbeatData = null;
		try {
			heartbeatData = engine.decode(dataBytes, HeartbeatData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回的解析数据
		List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
		// 基础信息
		DataPackObject dataPackObject = new DataPackObject(dataPack);
		// 设备ID
		dataPackObject.setDeviceId(heartbeatData.getHeader().getImei());
		// 设置数据采集时间
		dataPackObject.setDetectionTime(new Date());
		// 设置数据接收时间
		dataPackObject.setReceiveTime(new Date());
		// 设置数据加密方式
		dataPackObject.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(heartbeatData.getHeader().getEncryptType()));

		// 添加返回结果集
		DataPackHeartbeat dataPackHeartbeat = new DataPackHeartbeat(dataPackObject);
		dataPackTargetList.add(new DataPackTarget(dataPackHeartbeat));

		return dataPackTargetList;
	}

	@Override
	public byte[] encode(DataPack dataPack) {
		return null;
	}

}
