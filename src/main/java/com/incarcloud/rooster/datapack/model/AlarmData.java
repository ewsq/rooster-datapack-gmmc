package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Decimal;
import com.github.io.protocol.annotation.Number;

/**
 * @Title: AlarmData.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 车辆告警信息
 * @author: chenz
 * @date: 2017年11月27日 下午2:00:53
 * @version: V1.0
 */
public class AlarmData {
	/**
	 * 0:有效定位；1:无效定位（当数据通信正常，而不能获取定位信息时，发送最后一次有效定位信息，并将定位状态置为无效。）
	 */
	@Number(width = 1, order = ByteOrder.BigEndian)
	private Integer isValidate = 0;
	/**
	 * 0:北纬；1:南纬
	 */
	@Number(width = 1, order = ByteOrder.BigEndian)
	private Integer latType = 0;
	/**
	 * 0:东经；1:西经
	 */
	@Number(width = 1, order = ByteOrder.BigEndian)
	private Integer lngType = 0;
	/**
	 * 保留
	 */
	@Number(width = 5, order = ByteOrder.BigEndian)
	private Integer reserveBit = 0;
	/**
	 * 经度<br>
	 * 以度为单位的纬度值乘以10的6次方，精确到百万分之一度
	 */
	@Decimal(width = 32, order = ByteOrder.BigEndian, scale = 0.000001, precision = 6)
	private double longitude;
	/**
	 * 纬度<br>
	 * 以度为单位的纬度值乘以10的6次方，精确到百万分之一度
	 */
	@Decimal(width = 32, order = ByteOrder.BigEndian, scale = 0.000001, precision = 6)
	private double latitude;
	/**
	 * 速度<br>
	 * 有效值范围：0～2200（表示0km/h～220
	 * km/h），最小计量单元：0.1km/h。“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效。
	 */
	@Decimal(width = 16, order = ByteOrder.BigEndian, scale = 0.1)
	private float speed;
	/**
	 * 海拔<br>
	 * 以米为单位，最小计量单元：0.1m
	 */
	@Decimal(width = 32, order = ByteOrder.BigEndian, scale = 0.1)
	private float altitude;
	/**
	 * 方向<br>
	 * 有效值范围：0～359，正北为0，顺时针
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer direction;
	/**
	 * 保留位
	 */
	@Number(width = 32, order = ByteOrder.BigEndian)
	private long reserve;

	/**
	 * 安全气囊告警<br>
	 * 0:未触发,1:触发
	 */
	@Number(width = 8)
	private Integer ariBagAlarm;

	/**
	 * 碰撞告警<br>
	 * 0:未触发,1:触发
	 */
	@Number(width = 8)
	private Integer crashAlarm;

	/**
	 * 防盗告警<br>
	 * 0:未触发,1:触发
	 */
	@Number(width = 8)
	private Integer antiTheftAlarm;

	/**
	 * 灯光未关闭告警<br>
	 * 0:未触发,1:触发
	 */
	@Number(width = 8)
	private Integer lightNotClosedAlarm;

	/**
	 * 蓄电池电压异常告警<br>
	 * 0:未触发,1:触发
	 */
	@Number(width = 8)
	private Integer batteryVoltageAlarm;

	/**
	 * 保留位
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private long reserve1;

	public Integer getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(Integer isValidate) {
		this.isValidate = isValidate;
	}

	public Integer getLatType() {
		return latType;
	}

	public void setLatType(Integer latType) {
		this.latType = latType;
	}

	public Integer getLngType() {
		return lngType;
	}

	public void setLngType(Integer lngType) {
		this.lngType = lngType;
	}

	public Integer getReserveBit() {
		return reserveBit;
	}

	public void setReserveBit(Integer reserveBit) {
		this.reserveBit = reserveBit;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public long getReserve() {
		return reserve;
	}

	public void setReserve(long reserve) {
		this.reserve = reserve;
	}

	public Integer getAriBagAlarm() {
		return ariBagAlarm;
	}

	public void setAriBagAlarm(Integer ariBagAlarm) {
		this.ariBagAlarm = ariBagAlarm;
	}

	public Integer getCrashAlarm() {
		return crashAlarm;
	}

	public void setCrashAlarm(Integer crashAlarm) {
		this.crashAlarm = crashAlarm;
	}

	public Integer getAntiTheftAlarm() {
		return antiTheftAlarm;
	}

	public void setAntiTheftAlarm(Integer antiTheftAlarm) {
		this.antiTheftAlarm = antiTheftAlarm;
	}

	public Integer getLightNotClosedAlarm() {
		return lightNotClosedAlarm;
	}

	public void setLightNotClosedAlarm(Integer lightNotClosedAlarm) {
		this.lightNotClosedAlarm = lightNotClosedAlarm;
	}

	public Integer getBatteryVoltageAlarm() {
		return batteryVoltageAlarm;
	}

	public void setBatteryVoltageAlarm(Integer batteryVoltageAlarm) {
		this.batteryVoltageAlarm = batteryVoltageAlarm;
	}

	public long getReserve1() {
		return reserve1;
	}

	public void setReserve1(long reserve1) {
		this.reserve1 = reserve1;
	}

	@Override
	public String toString() {
		return "AlarmData [isValidate=" + isValidate + ", latType=" + latType + ", lngType=" + lngType + ", reserveBit="
				+ reserveBit + ", longitude=" + longitude + ", latitude=" + latitude + ", speed=" + speed
				+ ", altitude=" + altitude + ", direction=" + direction + ", reserve=" + reserve + ", ariBagAlarm="
				+ ariBagAlarm + ", crashAlarm=" + crashAlarm + ", antiTheftAlarm=" + antiTheftAlarm
				+ ", lightNotClosedAlarm=" + lightNotClosedAlarm + ", batteryVoltageAlarm=" + batteryVoltageAlarm
				+ ", reserve1=" + reserve1 + "]";
	}

}
