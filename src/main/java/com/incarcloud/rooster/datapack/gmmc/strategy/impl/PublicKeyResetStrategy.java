package com.incarcloud.rooster.datapack.gmmc.strategy.impl;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackActivation;
import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.gmmc.model.ActivationData;
import com.incarcloud.rooster.datapack.gmmc.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @Title: ActivationStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: tbox PublicKey重置解析策略
 * @author: chenz
 * @date: 2017年11月30日 下午3:10:14
 * @version: V1.0
 */
public class PublicKeyResetStrategy implements IDataPackStrategy {

	@Override
	public List<DataPackTarget> decode(DataPack dataPack) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
		// public key重置信息
		ActivationData activationData = null;
		try {
			activationData = engine.decode(dataBytes, ActivationData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回的解析数据
		List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
		// 基础信息
		DataPackObject dataPackObject = new DataPackObject(dataPack);
		// 设备ID
		dataPackObject.setDeviceId(activationData.getHeader().getImei());
		// vin
		dataPackObject.setVin(activationData.getVin());
		// 设置数据采集时间
		dataPackObject.setDetectionTime(new Date(activationData.getGatherTime()));
		// 设置数据接收时间
		dataPackObject.setReceiveTime(new Date());
		// 设置数据加密方式
		dataPackObject.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(activationData.getHeader().getEncryptType()));

		// 添加返回结果集
		DataPackActivation dataPackActivation = new DataPackActivation(dataPackObject);
		// 公钥
		dataPackActivation.setPublicKey(GmmcDataPackUtils.getBase64OfInt(activationData.getPublicKey()));
		// 公钥长度
		dataPackActivation.setLength(activationData.getPublicKeyLength());
		// 类型
		dataPackActivation.setActivationType(DataPackActivation.PUBLIC_KEY_RESET);

		dataPackTargetList.add(new DataPackTarget(dataPackActivation));

		return dataPackTargetList;
	}

	@Override
	public byte[] encode(DataPack dataPack) {
		return null;
	}

}
