package com.incarcloud.rooster.datapack;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * GMMC设备协议数据包解析器
 *
 * @author Aaric, created on 2017-11-22T17:54.
 * @since 2.0
 */
public class DataParserGmmc implements IDataParser {

    /**
     * Logger
     */
    private static Logger _logger = LoggerFactory.getLogger(DataParserGmmc.class);

    /**
     * 协议分组和名称
     */
    public static final String PROTOCOL_GROUP = "china";
    public static final String PROTOCOL_NAME = "gmmc";
    public static final String PROTOCOL_PREFIX = PROTOCOL_GROUP + "-" + PROTOCOL_NAME + "-";

    static {
        /**
         * 声明数据包版本与解析器类关系
         */
        DataParserManager.register(PROTOCOL_PREFIX + "0.3", DataParserGmmc.class);
    }

    @Override
    public List<DataPack> extract(ByteBuf buffer) {
        // TODO
        return null;
    }

    @Override
    public ByteBuf createResponse(DataPack requestPack, ERespReason reason) {
        // TODO
        return null;
    }

    @Override
    public void destroyResponse(ByteBuf responseBuf) {
        // TODO
    }

    @Override
    public List<DataPackTarget> extractBody(DataPack dataPack) {
        // TODO
        return null;
    }

    @Override
    public Map<String, Object> getMetaData(ByteBuf buffer) {
        // TODO
        return null;
    }
}
