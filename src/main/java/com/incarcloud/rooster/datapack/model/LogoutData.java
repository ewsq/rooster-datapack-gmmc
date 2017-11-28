package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.DateTime;
import com.github.io.protocol.annotation.Element;
import com.github.io.protocol.annotation.Number;

/**
 * @Title: LogoutData.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 车辆登出
 * @author: chenz
 * @date: 2017年11月27日 下午4:25:06
 * @version: V1.0
 */
public class LogoutData {
	/**
	 * 包头
	 */
	@Element
	private Header header;
	/**
	 * 登出时间
	 */
	@DateTime
	private Long logoutTime;
	/**
	 * 登出流水号
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer serialNumber;
	/**
	 * 包尾
	 */
	@Element
	private Tail tail = new Tail();

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Long getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Long logoutTime) {
		this.logoutTime = logoutTime;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Tail getTail() {
		return tail;
	}

	public void setTail(Tail tail) {
		this.tail = tail;
	}

	@Override
	public String toString() {
		return "LogoutData [header=" + header + ", logoutTime=" + logoutTime + ", serialNumber=" + serialNumber
				+ ", tail=" + tail + "]";
	}

}
