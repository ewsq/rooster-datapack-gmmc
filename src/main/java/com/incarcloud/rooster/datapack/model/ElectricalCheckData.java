package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.annotation.Number;

/**
 * @Title: ElectricalCheckData.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 电检
 * @author: chenz
 * @date: 2017年11月27日 下午2:57:56
 * @version: V1.0
 */
public class ElectricalCheckData {
	/**
	 * 数据采集时间
	 */
	@Number(width = 8)
	private Long gatherTime;

	public Long getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(Long gatherTime) {
		this.gatherTime = gatherTime;
	}

	@Override
	public String toString() {
		return "ElectricalCheckData [gatherTime=" + gatherTime + "]";
	}

}
