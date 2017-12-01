package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.annotation.AsciiString;

/**
 * @Title: ParamSettingData.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 参数设置/参数设置完成
 * @author: chenz
 * @date: 2017年11月27日 下午3:55:59
 * @version: V1.0
 */
public class ParameterSettingData {
	/**
	 * 参数版本<br>
	 * <p>
	 * 车辆当前参数版本
	 */
	@AsciiString(length = "10")
	private String parameterVersion;

	public String getParameterVersion() {
		return parameterVersion;
	}

	public void setParameterVersion(String parameterVersion) {
		this.parameterVersion = parameterVersion;
	}

	@Override
	public String toString() {
		return "ParameterSettingData [parameterVersion=" + parameterVersion + "]";
	}

}
