package com.incarcloud.rooster.datapack.model;

import java.util.Arrays;

/**
 * @Title: GmmcPakcage.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 报文包体结构
 * @author: chenz
 * @date: 2018年1月11日 下午5:01:54
 * @version: V1.0
 */
public class GmmcPackage {
	/**
	 * 包头
	 */
	private Header header;
	/**
	 * 包体
	 */
	private int[] bodyBuffer;
	/**
	 * 包尾
	 */
	private Tail tail;

	@Override
	public String toString() {
		return "GmmcPakcage [header=" + header + ", bodyBuffer=" + Arrays.toString(bodyBuffer) + ", tail=" + tail + "]";
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public int[] getBodyBuffer() {
		return bodyBuffer;
	}

	public void setBodyBuffer(int[] bodyBuffer) {
		this.bodyBuffer = bodyBuffer;
	}

	public Tail getTail() {
		return tail;
	}

	public void setTail(Tail tail) {
		this.tail = tail;
	}

}
