package com.incarcloud.rooster.datapack.model;

import com.github.io.protocol.core.ProtocolEngine;
import com.github.io.protocol.utils.HexStringUtil;
import com.incarcloud.rooster.datapack.GmmcCommandFactory;
import com.incarcloud.rooster.datapack.gmmc.model.ParameterSettingData;
import com.incarcloud.rooster.gather.cmd.CommandType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.StringUtil;
import org.junit.Ignore;
import org.junit.Test;

public class ParameterSettingDataTest {

	@Test
	@Ignore
	public void test() throws Exception {
		String s = "2323010121fe383632323334303231303432343" + "7300100581201120a0d300200010300010400010"
				+ "5000106000107320800010900010a00010b00010" + "c010d00010e330f0001100182000183018400018"
				+ "500018600018700018800018900018a00018b000" + "18c00018d348e00018f01990132";

		ParameterSettingData setting = new ParameterSettingData();
		ProtocolEngine engine = new ProtocolEngine();
		setting = engine.decode(HexStringUtil.parseBytes(s), ParameterSettingData.class);
		System.out.println(engine.toPrettyHexString(setting));
	}

	@Test
	@Ignore
	public void generateSetParam() throws Exception {
		int serialNumber = 100 ;
		String publicKey = "MTljMDIwMjAtYmVjYi00AA==" ;
		String deviceId = "862234021042470" ;

		Object[] args = new Object[33] ;
		args[0] = deviceId ;
		args[1] = serialNumber ;
		args[2] = publicKey ;
		args[3] = 1 ;
//                if (!StringUtil.isEmpty(deviceTboxParamRemoteVo.getParamVersion()))
//                    args[3] = deviceTboxParamRemoteVo.getParamVersion() ; //参数版本
		args[4] = 30 ; //本地数据存储周期
		//正常时，CAN信息上报时间周期
		args[5] = 100 ;
		//出现报警时，CAN信息上报时间周期
		args[6] = 30 ;

		//远程服务与管理平台域名或ip地址长度m
		String remoteSeverHost = "remoteHost" ;
		args[7] = remoteSeverHost.length() ;
		//远程服务与管理平台域名或ip地址
		args[8] = remoteSeverHost ;
		//远程服务与管理平台端口
		args[9] = 8887 ;

		//车载终端心跳发送周期
		args[10] = 100 ;
		//终端应答超时时间
		args[11] = 100 ;
		//平台应答超时时间
		args[12] = 100 ;
		//连续三次登入失败后，到下一次登入的间隔时间
		args[13] = 100 ;

		String platformGovHost = "govHost" ;
		args[14] = platformGovHost.length() ;
		//政府平台域名
		args[15] = platformGovHost ;
		//政府平台端口
		args[16] = 8889 ;

			//否处于抽样监测中“0x01”表示是，“0x02”表示否，“0xFE”表示异常，“0xFF”表示无效
		args[17] = 0x01 ;

		//延时工作模式下，CAN信息上报时间周期
		args[18] = 3000 ;
		//休眠时，心跳信息上报时间周期
		args[19] = 100 ;
		//休眠时，定位信息上报时间周期
		args[20] = 8000 ;
		//充电时，CAN信息上报时间周期
		args[21] = 100 ;
		//延时时间
		args[22] = 100 ;
		//正常时，定位信息上报时间周期，
		args[23] = 100 ;
		//出现报警时，定位信息上报时间周期
		args[24] = 100 ;
		//延时工作模式下，定位信息上报时间周期
		args[25] = 100 ;
		//充电时，定位信息上报时间周期
		args[26] = 100 ;
		//超速报警阀值
		args[27] = 100 ;

		//租赁平台域名或IP长度
		String rentPlatformHost = "rentalHost" ;
		args[28] = rentPlatformHost.length() ;
		//租赁平台域名或IP
		args[29] = rentPlatformHost ;
		//租赁平台端口
		args[30] = 8888 ;

		//APN，1移动CMNET，2联通UNINET 3 电信
		args[31] = 2 ;
		//PublicKey更新周期
		args[32] = 100 ;

		GmmcCommandFactory facotry = new GmmcCommandFactory();

		ByteBuf buffer = facotry.createCommand(CommandType.SET_PARAMS,args) ;
		byte[] bytes = ByteBufUtil.getBytes(buffer) ;
		System.out.println(HexStringUtil.toHexString(bytes));
	}
}
