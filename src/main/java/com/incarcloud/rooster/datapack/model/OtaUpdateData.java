package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.annotation.DateTime;
import com.github.io.protocol.annotation.Element;

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
	@Element
	private Header header;

	/**
	 * 数据采集时间
	 */
	@DateTime
	private long gatherTime;
	/**
	 * tbox当前软件版本
	 */
	@AsciiString(length = "10")
	private String softwareVersion;

	@Element
	private Tail tail;

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public long getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(long gatherTime) {
		this.gatherTime = gatherTime;
	}

	public Tail getTail() {
		return tail;
	}

	public void setTail(Tail tail) {
		this.tail = tail;
	}

	@Override
	public String toString() {
		return "OtaUpdateData [header=" + header + ", gatherTime=" + gatherTime + ", softwareVersion=" + softwareVersion
				+ ", tail=" + tail + "]";
	}

}
