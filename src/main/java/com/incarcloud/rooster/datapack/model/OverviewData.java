package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Decimal;
import com.github.io.protocol.annotation.Number;

/**
 * @Title: OverviewData.java
 * @Project: rooster-datapack-gmmc
 * @Package: com.incarcloud.rooster.datapack.model
 * @Description: 整车数据
 * @author: chenz
 * @date: 2017年11月29日 下午2:38:35
 * @version: V1.0
 */
public class OverviewData {
	/**
	 * <i>车辆状态</i><br>
	 * <ul>
	 * <li>0x01-发动机点火时</li>
	 * <li>0x02-发动机运行中</li>
	 * <li>0x03-发动机熄火时</li>
	 * <li>0x04-发动机熄火后</li>
	 * <li>0x05-车辆不能检测</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer carStatus;
	/**
	 * <i>充电状态</i><br>
	 * <ul>
	 * <li>0x01： 停车充电</li>
	 * <li>0x02： 行驶充电</li>
	 * <li>0x03： 未充电状态</li>
	 * <li>0x04： 充电完成</li>
	 * <li>0xFE：表示异常</li>
	 * <li>0xFF：表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer chargeStatus;

	/**
	 * <i>运行模式</i><br>
	 * <ul>
	 * <li>0x01: 纯电</li>
	 * <li>0x02：混动</li>
	 * <li>0x03：燃油</li>
	 * <li>0xFE：表示异常</li>
	 * <li>0xFF：表示无效</li>
	 * <ul>
	 */
	@Number(width = 8)
	private Integer runStatus;

	/**
	 * 实时车速<br>
	 * 有效值范围： 0～2200（表示 0 km/h～220 km/h）<br>
	 * 最小计量单元：0.1km/h<br>
	 * 0xFF,0xFE表示异常<br>
	 * 0xFF,0xFF表示无效<br>
	 */
	@Decimal(width = 16, order = ByteOrder.BigEndian, scale = 0.1)
	private float vehicleSpeed;

	/**
	 * 累计行驶里程(km)
	 */
	@Decimal(width = 32, order = ByteOrder.BigEndian, scale = 0.1)
	private Double mileage;

	/**
	 * 电压（V）
	 */
	@Decimal(width = 16, order = ByteOrder.BigEndian, scale = 0.1)
	private Float voltage;

	/**
	 * 电流（A）
	 */
	@Decimal(width = 16, order = ByteOrder.BigEndian, scale = 0.1)
	private Float current;

	/**
	 * SOC 有效值范围： 0～100（表示 0%～100%）<br>
	 * 最小计量单元： 1%<br>
	 * 0xFE 表示异常<br>
	 * 0xFF 表示无效<br>
	 */
	@Number(width = 8)
	private Integer soc;
	/**
	 * DC-DC 状态 0x01： 工作<br>
	 * 0x02： 断开<br>
	 * 0xFE表：示异常<br>
	 * 0xFF：表示无效<br>
	 */
	@Number(width = 8)
	private Integer dcdcStatus;
	/**
	 * <i>档位 bit0-bit3</i><br>
	 * <ul>
	 * <li>=0000 空档</li>
	 * <li>=0001 1 档</li>
	 * <li>=0010 2 档</li>
	 * <li>=0011 3 档</li>
	 * <li>=0100 4 档</li>
	 * <li>=0101 5 档</li>
	 * <li>=0110 6 档</li>
	 * <li>=1101 倒档</li>
	 * <li>=1110 自动 D 档</li>
	 * <li>=1111 停车 P 档</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer clutchStatus;

	/**
	 * 绝缘电阻<br>
	 * 有效范围 0~60000（表示 0KΩ~60000KΩ），最小计量单元： 1KΩ<br>
	 * 0xFF,0xFF 表示无效<br>
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer issueValue;

	/**
	 * 燃油量<br>
	 * 有效值范围：0～250，最小计量单元：1升<br>
	 * “0xFE”表示异常<br>
	 * “0xFF”表示无效<br>
	 */
	@Number(width = 8)
	private Integer fuelQuantity;

	/**
	 * 累计平均油耗<br>
	 * (0.01L/100km)/100F转Float
	 */
	@Decimal(width = 16, order = ByteOrder.BigEndian, scale = 0.01)
	private Float avgOilUsed;

	/**
	 * 左前轮胎压<br>
	 * 有效值范围：0～250，最小计量单元：kpa，“0xFE”表示异常，“0xFF”表示无效。
	 */
	@Number(width = 8)
	private Integer leftFrontTirePressure;

	/**
	 * 右前轮胎压<br>
	 * 有效值范围：0～250，最小计量单元：kpa，“0xFE”表示异常，“0xFF”表示无效。
	 */
	@Number(width = 8)
	private Integer rightFrontTirePressure;

	/**
	 * 左后轮胎压<br>
	 * 有效值范围：0～250，最小计量单元：kpa，“0xFE”表示异常，“0xFF”表示无效。
	 */
	@Number(width = 8)
	private Integer leftRearTirePressure;

	/**
	 * 右后轮胎压<br>
	 * 有效值范围：0～250，最小计量单元：kpa，“0xFE”表示异常，“0xFF”表示无效。
	 */
	@Number(width = 8)
	private Integer rightRearTirePressure;
	/**
	 * <i>左前门状态<i><br>
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer leftFrontDoorStatus;

	/**
	 * 左后门状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer leftBackDoorStatus;

	/**
	 * 右前门状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer rightFrontDoorStatus;

	/**
	 * 右后门状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer rightBackDoorStatus;

	/**
	 * 前盖状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer frontCover;

	/**
	 * 后备箱状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer trunkStatus;

	/**
	 * 左前窗状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer leftFrontWindowStatus;

	/**
	 * 右前窗状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer rightFrontWindowStatus;

	/**
	 * 左后窗状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer leftBackWindowStatus;

	/**
	 * 右后窗状态
	 * <ul>
	 * <li>0x01：开</li>
	 * <li>0x02：关</li>
	 * <li>“0xFE”表示异常，“0xFF”表示无效</li>
	 * </ul>
	 */
	@Number(width = 8)
	private Integer rightBackWindowStatus;

	/**
	 * 车外温度<br>
	 * 有效值范围：0～250 （数值偏移量40℃，表示-40℃～+210℃）<br>
	 * 最小计量单元：1℃<br>
	 * “0xFE”表示异常，“0xFF”表示无效<br>
	 */
	@Number(width = 8)
	private Integer OutsideTemperature;

	/**
	 * 车内温度<br>
	 * 有效值范围：0～250 （数值偏移量40℃，表示-40℃～+210℃）<br>
	 * 最小计量单元：1℃<br>
	 * “0xFE”表示异常，“0xFF”表示无效<br>
	 */
	@Number(width = 8)
	private Integer insideTemperature;

	/**
	 * 续航里程 有效值范围： 0～20000<br>
	 * 单位：公里<br>
	 * 0xFF,0xFE 表示异常<br>
	 * 0xFF,0xFF 表示无效<br>
	 */
	@Number(width = 8)
	private Integer rechargeMileage;

	/**
	 * 预留字段
	 */
	@Number(width = 16, order = ByteOrder.BigEndian)
	private Integer reserve;
	
	

	public Integer getFrontCover() {
		return frontCover;
	}

	public void setFrontCover(Integer frontCover) {
		this.frontCover = frontCover;
	}

	public Integer getLeftFrontTirePressure() {
		return leftFrontTirePressure;
	}

	public void setLeftFrontTirePressure(Integer leftFrontTirePressure) {
		this.leftFrontTirePressure = leftFrontTirePressure;
	}

	public Integer getRightFrontTirePressure() {
		return rightFrontTirePressure;
	}

	public void setRightFrontTirePressure(Integer rightFrontTirePressure) {
		this.rightFrontTirePressure = rightFrontTirePressure;
	}

	public Integer getLeftRearTirePressure() {
		return leftRearTirePressure;
	}

	public void setLeftRearTirePressure(Integer leftRearTirePressure) {
		this.leftRearTirePressure = leftRearTirePressure;
	}

	public Integer getRightRearTirePressure() {
		return rightRearTirePressure;
	}

	public void setRightRearTirePressure(Integer rightRearTirePressure) {
		this.rightRearTirePressure = rightRearTirePressure;
	}

	public Integer getLeftFrontDoorStatus() {
		return leftFrontDoorStatus;
	}

	public void setLeftFrontDoorStatus(Integer leftFrontDoorStatus) {
		this.leftFrontDoorStatus = leftFrontDoorStatus;
	}

	public Integer getRightFrontDoorStatus() {
		return rightFrontDoorStatus;
	}

	public void setRightFrontDoorStatus(Integer rightFrontDoorStatus) {
		this.rightFrontDoorStatus = rightFrontDoorStatus;
	}

	public Integer getLeftBackDoorStatus() {
		return leftBackDoorStatus;
	}

	public void setLeftBackDoorStatus(Integer leftBackDoorStatus) {
		this.leftBackDoorStatus = leftBackDoorStatus;
	}

	public Integer getRightBackDoorStatus() {
		return rightBackDoorStatus;
	}

	public void setRightBackDoorStatus(Integer rightBackDoorStatus) {
		this.rightBackDoorStatus = rightBackDoorStatus;
	}

	public Integer getTrunkStatus() {
		return trunkStatus;
	}

	public void setTrunkStatus(Integer trunkStatus) {
		this.trunkStatus = trunkStatus;
	}

	public Integer getLeftFrontWindowStatus() {
		return leftFrontWindowStatus;
	}

	public void setLeftFrontWindowStatus(Integer leftFrontWindowStatus) {
		this.leftFrontWindowStatus = leftFrontWindowStatus;
	}

	public Integer getRightFrontWindowStatus() {
		return rightFrontWindowStatus;
	}

	public void setRightFrontWindowStatus(Integer rightFrontWindowStatus) {
		this.rightFrontWindowStatus = rightFrontWindowStatus;
	}

	public Integer getLeftBackWindowStatus() {
		return leftBackWindowStatus;
	}

	public void setLeftBackWindowStatus(Integer leftBackWindowStatus) {
		this.leftBackWindowStatus = leftBackWindowStatus;
	}

	public Integer getRightBackWindowStatus() {
		return rightBackWindowStatus;
	}

	public void setRightBackWindowStatus(Integer rightBackWindowStatus) {
		this.rightBackWindowStatus = rightBackWindowStatus;
	}

	public Integer getOutsideTemperature() {
		return OutsideTemperature;
	}

	public void setOutsideTemperature(Integer outsideTemperature) {
		OutsideTemperature = outsideTemperature;
	}

	public Integer getInsideTemperature() {
		return insideTemperature;
	}

	public void setInsideTemperature(Integer insideTemperature) {
		this.insideTemperature = insideTemperature;
	}

	public Integer getRechargeMileage() {
		return rechargeMileage;
	}

	public void setRechargeMileage(Integer rechargeMileage) {
		this.rechargeMileage = rechargeMileage;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public Integer getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(Integer carStatus) {
		this.carStatus = carStatus;
	}

	public Integer getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(Integer chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public Integer getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(Integer runStatus) {
		this.runStatus = runStatus;
	}

	public float getVehicleSpeed() {
		return vehicleSpeed;
	}

	public void setVehicleSpeed(float vehicleSpeed) {
		this.vehicleSpeed = vehicleSpeed;
	}

	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}

	public Float getVoltage() {
		return voltage;
	}

	public void setVoltage(Float voltage) {
		this.voltage = voltage;
	}

	public Float getCurrent() {
		return current;
	}

	public void setCurrent(Float current) {
		this.current = current;
	}

	public Integer getSoc() {
		return soc;
	}

	public void setSoc(Integer soc) {
		this.soc = soc;
	}

	public Integer getDcdcStatus() {
		return dcdcStatus;
	}

	public void setDcdcStatus(Integer dcdcStatus) {
		this.dcdcStatus = dcdcStatus;
	}

	public Integer getClutchStatus() {
		return clutchStatus;
	}

	public void setClutchStatus(Integer clutchStatus) {
		this.clutchStatus = clutchStatus;
	}

	public Integer getIssueValue() {
		return issueValue;
	}

	public void setIssueValue(Integer issueValue) {
		this.issueValue = issueValue;
	}

	public Integer getFuelQuantity() {
		return fuelQuantity;
	}

	public void setFuelQuantity(Integer fuelQuantity) {
		this.fuelQuantity = fuelQuantity;
	}

	public Float getAvgOilUsed() {
		return avgOilUsed;
	}

	public void setAvgOilUsed(Float avgOilUsed) {
		this.avgOilUsed = avgOilUsed;
	}

	@Override
	public String toString() {
		return "OverviewData [carStatus=" + carStatus + ", chargeStatus=" + chargeStatus + ", runStatus=" + runStatus
				+ ", vehicleSpeed=" + vehicleSpeed + ", mileage=" + mileage + ", voltage=" + voltage + ", current="
				+ current + ", soc=" + soc + ", dcdcStatus=" + dcdcStatus + ", clutchStatus=" + clutchStatus
				+ ", issueValue=" + issueValue + ", fuelQuantity=" + fuelQuantity + ", avgOilUsed=" + avgOilUsed
				+ ", leftFrontTirePressure=" + leftFrontTirePressure + ", rightFrontTirePressure="
				+ rightFrontTirePressure + ", leftRearTirePressure=" + leftRearTirePressure + ", rightRearTirePressure="
				+ rightRearTirePressure + ", leftFrontDoorStatus=" + leftFrontDoorStatus + ", leftBackDoorStatus="
				+ leftBackDoorStatus + ", rightFrontDoorStatus=" + rightFrontDoorStatus + ", rightBackDoorStatus="
				+ rightBackDoorStatus + ", frontCover=" + frontCover + ", trunkStatus=" + trunkStatus
				+ ", leftFrontWindowStatus=" + leftFrontWindowStatus + ", rightFrontWindowStatus="
				+ rightFrontWindowStatus + ", leftBackWindowStatus=" + leftBackWindowStatus + ", rightBackWindowStatus="
				+ rightBackWindowStatus + ", OutsideTemperature=" + OutsideTemperature + ", insideTemperature="
				+ insideTemperature + ", rechargeMileage=" + rechargeMileage + ", reserve=" + reserve + "]";
	}
	
	

}
