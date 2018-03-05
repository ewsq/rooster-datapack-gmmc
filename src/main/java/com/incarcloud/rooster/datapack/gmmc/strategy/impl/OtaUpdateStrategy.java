package com.incarcloud.rooster.datapack.gmmc.strategy.impl;

import com.github.io.protocol.core.ProtocolEngine;
import com.incarcloud.rooster.datapack.DataPack;
import com.incarcloud.rooster.datapack.DataPackObject;
import com.incarcloud.rooster.datapack.DataPackTarget;
import com.incarcloud.rooster.datapack.DataPackTrip;
import com.incarcloud.rooster.datapack.gmmc.model.OtaUpdateResp;
import com.incarcloud.rooster.datapack.gmmc.strategy.IDataPackStrategy;
import com.incarcloud.rooster.datapack.gmmc.utils.GmmcDataPackUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Created by Kong on 2018/3/2.
 */
public class OtaUpdateStrategy implements IDataPackStrategy {

    @Override
    public List<DataPackTarget> decode(DataPack dataPack) {
        // 解析器
        ProtocolEngine engine = new ProtocolEngine();
        // 获取解析数据byte数组
        byte[] dataBytes = Base64.getDecoder().decode(dataPack.getDataB64());
        // OTA升级完成
        OtaUpdateResp otaUpdateResp = null ;

        try {
            otaUpdateResp = engine.decode(dataBytes, OtaUpdateResp.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回的解析数据
        List<DataPackTarget> dataPackTargetList = new ArrayList<DataPackTarget>();
        // 基础信息
        DataPackObject dataPackObject = new DataPackObject(dataPack);

        // 设备ID
        dataPackObject.setDeviceId(otaUpdateResp.getHeader().getImei());
        // 设置数据采集时间
        dataPackObject.setDetectionTime(new Date(otaUpdateResp.getGatherTime()));
        // 设置数据接收时间
        dataPackObject.setReceiveTime(new Date());
        // 设置数据加密方式
        dataPackObject.setEncryptName(GmmcDataPackUtils.getMsgEncryptMode(otaUpdateResp.getHeader().getEncryptType()));

//        // 构建返回的数据
//        OtaUpdateResp dataPackTrip = new OtaUpdateResp(dataPackObject);
//        dataPackTrip.set
//
//        // 添加返回结果集
//        dataPackTargetList.add(new DataPackTarget(dataPackTrip));

        return null;
    }

    @Override
    public byte[] encode(DataPack dataPack) {
        return new byte[0];
    }

}
