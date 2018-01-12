package com.incarcloud.rooster.datapack.strategy.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackActivation;
import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.model.ActivationData;
import com.incarcloud.rooster.datapack.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

/**
 * @Title: ActivationStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: tbox激活报文解析策略
 * @author: chenz
 * @date: 2017年11月30日 下午3:10:14
 * @version: V1.0
 */
public class ActivationStrategy implements IDataPackStrategy {

	/**
	 * 
	 * @param dataPack
	 *            报文
	 * @param key
	 *            uuid 使用uuid的MD5作为AES128解密密钥
	 */
	@Override
	public List<DataPackTarget> decode(DataPack dataPack, String key) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
		// tbox激活信息
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
		// 公钥 用uuid的MD5（32位大写）作为AES128解密秘钥
//		byte[] publicKeyDecBytes = RSAEncrypt.AESDecode(key.getBytes(),
//				GmmcDataPackUtils.coverToByteArray(activationData.getPublicKey()));
//		dataPackActivation.setPublicKey(Base64.getEncoder().encodeToString(publicKeyDecBytes));	
		dataPackActivation.setPublicKey(Base64.getEncoder().encodeToString(GmmcDataPackUtils.coverToByteArray(activationData.getPublicKey())));
		// 公钥长度
		dataPackActivation.setLength(activationData.getPublicKeyLength());
		// 类型
		dataPackActivation.setActivationType(DataPackActivation.ACTIVATION);

		dataPackTargetList.add(new DataPackTarget(dataPackActivation));

		return dataPackTargetList;
	}

	@Override
	public byte[] encode(DataPack dataPack, String key) {
		return null;
	}

}
