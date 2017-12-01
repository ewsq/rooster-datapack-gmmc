package com.incarcloud.rooster.datapack.model;

import java.util.Arrays;

import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Element;
import com.github.io.protocol.annotation.Number;

/**
 * @Title: ActivationData.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: tbox激活/publicKey重置
 * @author: chenz
 * @date: 2017年11月27日 下午3:13:17
 * @version: V1.0
 */
public class ActivationData {
	/**
	 * 包头
	 */
	@Element
	private Header header;
	/**
	 * 数据采集时间
	 */
	@Number(width = 8)
	private Long gatherTime;
	/**
	 * 公钥长度<br>
	 * TBox公钥长度。范围（0-255）
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer publicKeyLength;
	/**
	 * 公钥<br>
	 * TBox公钥,长度为公钥长度定义的长度
	 */
	@Number(width = 8, length = "getKeyLength")
	private byte[] publicKey;
	/**
	 * vin<br>
	 * 车辆识别码是识别的唯一标识，由17位字码构成
	 */
	@AsciiString(length = "17")
	private String vin;

	/**
	 * 包尾
	 */
	@Element
	private Tail tail;

	/**
	 * 获取公钥长度
	 * 
	 * @return
	 */
	public Integer getKeyLength() {
		return publicKeyLength;
	}

	public Long getGatherTime() {
		return gatherTime;
	}

	public void setGatherTime(Long gatherTime) {
		this.gatherTime = gatherTime;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Tail getTail() {
		return tail;
	}

	public void setTail(Tail tail) {
		this.tail = tail;
	}

	public Integer getPublicKeyLength() {
		return publicKeyLength;
	}

	public void setPublicKeyLength(Integer publicKeyLength) {
		this.publicKeyLength = publicKeyLength;
	}

	public byte[] getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(byte[] publicKey) {
		this.publicKey = publicKey;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	@Override
	public String toString() {
		return "ActivationData [header=" + header + ", gatherTime=" + gatherTime + ", publicKeyLength="
				+ publicKeyLength + ", publicKey=" + Arrays.toString(publicKey) + ", vin=" + vin + ", tail=" + tail
				+ "]";
	}

}
