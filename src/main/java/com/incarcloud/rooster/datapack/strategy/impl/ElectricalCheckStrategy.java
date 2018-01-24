package com.incarcloud.rooster.datapack.strategy.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackElectricalCheck;
import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.model.ElectricalCheckData;
import com.incarcloud.rooster.datapack.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

/**
 * @Title: ElectricalCheckStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: 车辆电检解析策略
 * @author: chenz
 * @date: 2017年11月30日 下午2:58:41
 * @version: V1.0
 */
public class ElectricalCheckStrategy implements IDataPackStrategy {
	/**
	 * 报文解析
	 */
	@Override
	public List<DataPackTarget> decode(DataPack dataPack) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
		// 车辆电检信息
		ElectricalCheckData electricalCheckData = null;
		try {
			electricalCheckData = engine.decode(dataBytes, ElectricalCheckData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回的解析数据
		List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
		// 基础信息
		DataPackObject dataPackObject = new DataPackObject(dataPack);
		// 设备ID
		dataPackObject.setDeviceId(electricalCheckData.getHeader().getImei());
		// 设置数据采集时间
		dataPackObject.setDetectionTime(new Date(electricalCheckData.getGatherTime()));
		// 设置数据接收时间
		dataPackObject.setReceiveTime(new Date());
		// 设置数据加密方式
		dataPackObject
				.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(electricalCheckData.getHeader().getEncryptType()));
		// 添加返回结果集
		DataPackElectricalCheck dataPackElectricalCheck = new DataPackElectricalCheck(dataPackObject);
		dataPackTargetList.add(new DataPackTarget(dataPackElectricalCheck));

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
