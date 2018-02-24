package com.incarcloud.rooster.datapack.gmmc.strategy.impl;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.*;
import com.incarcloud.rooster.datapack.gmmc.model.ActivationData;
import com.incarcloud.rooster.datapack.gmmc.model.SecurityData;
import com.incarcloud.rooster.datapack.gmmc.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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

	DataParserGmmc dataParserGmmc ;

	public void setDataParserGmmc(DataParserGmmc dataParserGmmc){
		if (null == dataParserGmmc){
			this.dataParserGmmc = dataParserGmmc ;
		}
	}

	@Override
	public List<DataPackTarget> decode(DataPack dataPack) {
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
		String deviceId = activationData.getHeader().getImei() ;

		dataPackObject.setDeviceId(deviceId);
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

		SecurityData securityData = DataParserGmmc.getSecurityData(deviceId) ;

		if (null != securityData){

			dataPackActivation.setPublicKeyModulusBytes(securityData.getPublicKeyModulusBytes());
			dataPackActivation.setPublicKeyExponent(securityData.getPublicKeyExponent());
			// 类型
			dataPackActivation.setActivationType(DataPackActivation.ACTIVATION);

			dataPackTargetList.add(new DataPackTarget(dataPackActivation));
		}

		return dataPackTargetList;
	}

	@Override
	public byte[] encode(DataPack dataPack) {
		return null;
	}

}
