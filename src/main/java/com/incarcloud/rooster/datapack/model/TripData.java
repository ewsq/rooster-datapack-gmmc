package com.incarcloud.rooster.datapack.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.DateTime;
import com.github.io.protocol.annotation.Decimal;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;

/**
 * @Title: TripData.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 车辆行程数据
 * @author: chenz
 * @date: 2017年11月27日 上午10:05:45
 * @version: V1.0
 */
public class TripData {
	/**
	 * 行程开始时间
	 */
	@DateTime
	private long startTime;
	/**
	 * 行程结束时间
	 */
	@DateTime
	private long endTime;
	/**
	 * 平均油耗<br>
	 * 有效值范围： 0～1000，（数值偏移量n/10，表示
	 * 0～100升/百公里），最小计量单元：0.1升/百公里，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效。
	 */
	@Decimal(width = 16, order = ByteOrder.BigEndian, scale = 0.1)
	private Integer oilWearAvg;

	/**
	 * 平均车速<br>
	 * 有效值范围： 0～1000单位：公里/小时，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer speedAvg;
	/**
	 * 最高车速<br>
	 * 有效值范围： 0～1000单位：公里/小时，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer speedMax;
	/**
	 * 行程时长<br>
	 * 有效值范围： 0～500000单位：分钟，“0xFE”表示异常，“0xFF”表示无效。
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer tripDuration;
	/**
	 * 行程行驶里程<br>
	 * 有效值范围： 0～100000单位：公里，“0xFF,0xFE”表示异常，“0xFF,0xFF”表示无效
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer mileage;
	/**
	 * 急加速次数<br>
	 * 有效值范围： 0～255单位：次，“0xFE”表示异常，“0xFF”表示无效。
	 */
	@Number(width = 8)
	private Integer rapidAccelerationTimes;
	/**
	 * 急减速次数<br>
	 * 有效值范围： 0～255单位：次，“0xFE”表示异常，“0xFF”表示无效。
	 */
	@Number(width = 8)
	private Integer rapidDecelerationTimes;
	/**
	 * 急转弯次数<br>
	 * 有效值范围： 0～255单位：次，“0xFE”表示异常，“0xFF”表示无效。
	 */
	@Number(width = 8)
	private Integer sharpTurnTimes;

	public Integer getOilWearAvg() {
		return oilWearAvg;
	}

	public void setOilWearAvg(Integer oilWearAvg) {
		this.oilWearAvg = oilWearAvg;
	}

	public Integer getSpeedAvg() {
		return speedAvg;
	}

	public void setSpeedAvg(Integer speedAvg) {
		this.speedAvg = speedAvg;
	}

	public Integer getSpeedMax() {
		return speedMax;
	}

	public void setSpeedMax(Integer speedMax) {
		this.speedMax = speedMax;
	}

	public Integer getTripDuration() {
		return tripDuration;
	}

	public void setTripDuration(Integer tripDuration) {
		this.tripDuration = tripDuration;
	}

	public Integer getMileage() {
		return mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	public Integer getRapidAccelerationTimes() {
		return rapidAccelerationTimes;
	}

	public void setRapidAccelerationTimes(Integer rapidAccelerationTimes) {
		this.rapidAccelerationTimes = rapidAccelerationTimes;
	}

	public Integer getRapidDecelerationTimes() {
		return rapidDecelerationTimes;
	}

	public void setRapidDecelerationTimes(Integer rapidDecelerationTimes) {
		this.rapidDecelerationTimes = rapidDecelerationTimes;
	}

	public Integer getSharpTurnTimes() {
		return sharpTurnTimes;
	}

	public void setSharpTurnTimes(Integer sharpTurnTimes) {
		this.sharpTurnTimes = sharpTurnTimes;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "TripData [startTime=" + startTime + ", endTime=" + endTime + ", oilWearAvg=" + oilWearAvg
				+ ", speedAvg=" + speedAvg + ", speedMax=" + speedMax + ", tripDuration=" + tripDuration + ", mileage="
				+ mileage + ", rapidAccelerationTimes=" + rapidAccelerationTimes + ", rapidDecelerationTimes="
				+ rapidDecelerationTimes + ", sharpTurnTimes=" + sharpTurnTimes + "]";
	}

	public static void main(String[] args) throws Exception {
		ProtocolEngine engine = new ProtocolEngine();
		TripData data = new TripData();
		data.setEndTime(1495550807000L);
		data.setStartTime(1495550807000L);

		Date date = new Date(1495550807000l);
		Instant timestamp = date.toInstant();
		LocalDateTime time = LocalDateTime.ofInstant(timestamp, ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
		System.out.println(time.getYear());
		System.out.println(time.getMonth().getValue());
		System.out.println(time.getDayOfMonth());
		System.out.println(time.getHour());
		System.out.println(time.getMinute());
		System.out.println(time.getSecond());

		System.out.println(HexStringUtil.toHexString(engine.encode(data)));
	}

}
