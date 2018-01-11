package com.incarcloud.rooster.datapack.strategy.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackLogInOut;
import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.model.LoginData;
import com.incarcloud.rooster.datapack.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.utils.GmmcDataPackUtils;

/**
 * @Title: LoginDataStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy.impl
 * @Description: 登录报文解析策略
 * @author: chenz
 * @date: 2017年11月28日 下午5:08:33
 * @version: V1.0
 */
public class LoginDataStrategy implements IDataPackStrategy {

	/**
	 * 解包
	 *
	 * @param dataPack
	 *            报文数据
	 */
	@Override
	public List<DataPackTarget> decode(DataPack dataPack, String key) {
		// 解析器
		ProtocolEngine engine = new ProtocolEngine();
		// 获取解析数据byte数组
		byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());

		// 对报文体做AES128解密

		LoginData loginData = null;
		try {
			loginData = engine.decode(dataBytes, LoginData.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回的解析数据
		List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
		// 基础信息
		DataPackObject dataPackObject = new DataPackObject(dataPack);
		// 设备ID
		dataPackObject.setDeviceId(loginData.getHeader().getImei());
		// 设置数据采集时间
		dataPackObject.setDetectionTime(new Date(loginData.getGatherTime()));
		// 设置数据接收时间
		dataPackObject.setReceiveTime(new Date());
		// 设置数据加密方式
		dataPackObject.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(loginData.getHeader().getEncryptType()));
		// 车辆vin
		dataPackObject.setVin(loginData.getVin());
		// 构建返回数据结构
		DataPackLogInOut logInOut = new DataPackLogInOut(dataPackObject);
		// 设置登录类型 0 登录 1 登出
		logInOut.setLoginType(DataPackLogInOut.LOGIN_TYPE_LOGIN);
		// 可充电储能子系统数
		logInOut.setSysNumber(loginData.getElectricalCount());
		// 可充电储能系统编码长度
		logInOut.setCodeLength(loginData.getElectricalLenth());
		// 可充电储能系统编码
		logInOut.setSysCode(loginData.getElectricalSysCode());
		// 登入登出流水号
		logInOut.setSerialNo(loginData.getSerialNumber());
		// 加密密钥-base64字符串
		logInOut.setSecurityKey(GmmcDataPackUtils.getBase64OfInt(loginData.getSecurityKey()));
		dataPackTargetList.add(new DataPackTarget(logInOut));
		return dataPackTargetList;
	}

	/**
	 * 封包
	 *
	 * @param dataPack
	 *            报文封装类
	 * @return
	 */
	@Override
	public byte[] encode(DataPack dataPack, String key) {
		return null;
	}

}
