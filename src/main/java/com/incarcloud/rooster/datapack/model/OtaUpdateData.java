package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.annotation.AsciiString;

/**
 * @Title: OtaUpdateData.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: ota升级/ota升级完成
 * @author: chenz
 * @date: 2017年11月27日 下午4:09:30
 * @version: V1.0
 */
public class OtaUpdateData {
	/**
	 * tbox当前软件版本
	 */
	@AsciiString(length = "10")
	private String sofrwareVersion;

	public String getSofrwareVersion() {
		return sofrwareVersion;
	}

	public void setSofrwareVersion(String sofrwareVersion) {
		this.sofrwareVersion = sofrwareVersion;
	}

	@Override
	public String toString() {
		return "OtaUpdateData [sofrwareVersion=" + sofrwareVersion + "]";
	}

}
