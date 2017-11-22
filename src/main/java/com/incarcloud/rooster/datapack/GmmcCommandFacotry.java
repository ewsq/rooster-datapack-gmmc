package com.incarcloud.rooster.datapack;

import com.incarcloud.rooster.gather.cmd.CommandFacotryManager;
import com.incarcloud.rooster.gather.cmd.CommandFactory;
import com.incarcloud.rooster.gather.cmd.CommandType;
import io.netty.buffer.ByteBuf;

/**
 * GMMC设备协议命令工厂
 *
 * @author Aaric, created on 2017-11-22T17:54.
 * @since 2.0
 */
public class GmmcCommandFacotry implements CommandFactory {

    static {
        /**
         * 声明数据包版本与命令工厂类关系
         */
        CommandFacotryManager.registerCommandFacotry(DataParserGmmc.PROTOCOL_PREFIX + "0.3", GmmcCommandFacotry.class);
    }

    @Override
    public ByteBuf createCommand(CommandType type, Object... args) throws Exception {
        // TODO
        return null;
    }
}
