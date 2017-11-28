package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;

/**
 * @Title: Header.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 包头
 * @author: chenz
 * @date: 2017年11月24日 下午4:03:16
 * @version: V1.0
 */
public class Header {
	/**
	 * 起始符 固定为ASCII字符‘##’，用“0x23,0x23”表示
	 */
	@Number(width = 16)
	private Integer startFiled = 0x2323;

	/**
	 * 车辆类型标识 0x01燃油车 ；0x02 新能源车
	 */
	@Number(width = 8)
	private Integer carFlag;

	/**
	 * 车型 0x01 ZC；0xRE NS；0x03 NS
	 */
	@Number(width = 8)
	private Integer carType;

	/**
	 * 命令标识
	 */
	@Number(width = 8)
	private Integer cmdFlag;

	/**
	 * 应答标志
	 */
	@Number(width = 8)
	private Integer responeFlag;

	/**
	 * IMEI TBOX硬件的IMEI号码
	 */
	@AsciiString(length = "15")
	private String imei;

	/**
	 * 数据加密方式 0x00：数据不加密；0x01：数据经过RSA算法加密；0xFF：无效数据；其他预留
	 */
	@Number(width = 8)
	private Integer encryptType;

	/**
	 * 数据单元长度 数据单元长度是数据单元的总字节数，有效值范围：0～65534
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer length = 0;

	/**
	 * 默认构造器
	 */
	public Header() {
	}

	public static void main(String[] args) throws Exception {
		ProtocolEngine engine = new ProtocolEngine();
		Header header = new Header();
		header.setCmdFlag(0x11);
		header.setCarType(0x01);
		header.setEncryptType(0x01);
		header.setCarFlag(0x01);
	//	header.setGatherTime(System.currentTimeMillis());
		header.setImei("000000000000000");
		header.setLength(0);
		header.setResponeFlag(0x01);
		header.setCarFlag(0x01);
		byte[] bytes = engine.encode(header);
		System.out.println(HexStringUtil.toHexString(engine.encode(header)));
		Header h1 = engine.decode(bytes, Header.class);
		System.out.println(h1.getImei());

	}

	@Override
	public String toString() {
		return "Header [startFiled=" + startFiled + ", carFlag=" + carFlag + ", carType=" + carType + ", cmdFlag="
				+ cmdFlag + ", responeFlag=" + responeFlag + ", imei=" + imei + ", encryptType=" + encryptType
				+ ", length=" + length + "]";
	}

	public Integer getStartFiled() {
		return startFiled;
	}

	public void setStartFiled(Integer startFiled) {
		this.startFiled = startFiled;
	}

	public Integer getCarFlag() {
		return carFlag;
	}

	public void setCarFlag(Integer carFlag) {
		this.carFlag = carFlag;
	}

	public Integer getCarType() {
		return carType;
	}

	public void setCarType(Integer carType) {
		this.carType = carType;
	}

	public Integer getCmdFlag() {
		return cmdFlag;
	}

	public void setCmdFlag(Integer cmdFlag) {
		this.cmdFlag = cmdFlag;
	}

	public Integer getResponeFlag() {
		return responeFlag;
	}

	public void setResponeFlag(Integer responeFlag) {
		this.responeFlag = responeFlag;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Integer getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(Integer encryptType) {
		this.encryptType = encryptType;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}
}
