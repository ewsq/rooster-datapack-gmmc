package com.incarcloud.rooster.datapack.gmmc.model;

import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.annotation.Element;
import com.github.io.protocol.annotation.Number;

/**
 * @Title: OtaUpdateResp.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: ota升级反馈
 * @author: chenz
 * @date: 2018年1月9日 下午3:02:54
 * @version: V1.0
 */
public class OtaUpdateResp {
	@Element
	private Header Header;
	/**
	 * TBOX当前版本号
	 */
	@AsciiString(length = "10")
	private String softwareVersion;
	/**
	 * 升级包文件名
	 */
	@AsciiString(length = "20")
	private String updatePackageName;
	/**
	 * 是否需要升级.0 不需要;1 需要
	 */
	@Number(width = 8)
	private int flag;
	/**
	 * 下载链接URL
	 */
	@AsciiString(length = "200")
	private String url;

	@Element
	private Tail tail;

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getUpdatePackageName() {
		return updatePackageName;
	}

	public void setUpdatePackageName(String updatePackageName) {
		this.updatePackageName = updatePackageName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Header getHeader() {
		return Header;
	}

	public void setHeader(Header header) {
		Header = header;
	}

	public Tail getTail() {
		return tail;
	}

	public void setTail(Tail tail) {
		this.tail = tail;
	}

	@Override
	public String toString() {
		return "OtaUpdateResp [Header=" + Header + ", softwareVersion=" + softwareVersion + ", updatePackageName="
				+ updatePackageName + ", flag=" + flag + ", url=" + url + ", tail=" + tail + "]";
	}

}
