package com.incarcloud.rooster.datapack.gmmc.strategy.impl;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackLogInOut;
import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.gmmc.model.LogoutData;
import com.incarcloud.rooster.datapack.gmmc.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * @Title: LogoutDataStartegy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: 车辆登出解析策略
 * @author: chenz
 * @date: 2017年11月29日 下午4:46:33
 * @version: V1.0
 */
public class LogoutDataStrategy implements IDataPackStrategy {

	/**
	 * 解析登出报文信息
	 */
	@Override
	public List<DataPackTarget> decode(DataPack dataPack) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
		LogoutData logoutData = null;
		try {
			logoutData = engine.decode(dataBytes, LogoutData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回的解析数据
		List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
		// 基础信息
		DataPackObject dataPackObject = new DataPackObject(dataPack);

		// 设备ID
		dataPackObject.setDeviceId(logoutData.getHeader().getImei());
		// 设置数据采集时间
		dataPackObject.setDetectionTime(new Date(logoutData.getLogoutTime()));
		// 设置数据接收时间
		dataPackObject.setReceiveTime(new Date());
		// 设置数据加密方式
		dataPackObject.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(logoutData.getHeader().getEncryptType()));

		// 构建登出数据
		DataPackLogInOut logout = new DataPackLogInOut(dataPackObject);
		logout.setLoginType(DataPackLogInOut.LOGIN_TYPE_LOGOUT);// 登录类型
		logout.setSerialNo(logoutData.getSerialNumber());// 登出流水号
		// 添加结果集
		dataPackTargetList.add(new DataPackTarget(logout));
		return dataPackTargetList;
	}

	@Override
	public byte[] encode(DataPack dataPack) {
		return null;
	}

}
