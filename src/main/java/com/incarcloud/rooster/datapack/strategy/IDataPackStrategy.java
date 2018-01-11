package com.incarcloud.rooster.datapack.strategy;

import java.util.List;

import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackTarget;

/**
 * @Title: IDataPackStrategy.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.strategy
 * @Description: 协议解析策略接口
 * @author: chenz
 * @date: 2017年11月28日 下午4:54:21
 * @version: V1.0
 */
public interface IDataPackStrategy {
	/**
	 * 解析报文数据
	 * 
	 * @param datapack
	 *            报文数据包
	 * @param key
	 *            密钥
	 * @return
	 */
	public List<DataPackTarget> decode(DataPack dataPack, String key);

	/**
	 * 封装报文
	 * 
	 * @param dataPack
	 *            下行指令
	 * @param key
	 *            密钥
	 * @return
	 */
	public byte[] encode(DataPack dataPack, String key);
}
