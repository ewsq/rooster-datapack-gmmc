package com.incarcloud.rooster.datapack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Base64;
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
	public static final String PROTOCOL_VERSION = "2017.11";
	public static final String PROTOCOL_PREFIX = PROTOCOL_GROUP + "-" + PROTOCOL_NAME + "-";

	// 国标协议最小长度
	private static final int GB_LENGTH = 25;

	static {
		/**
		 * 声明数据包版本与解析器类关系
		 */
		DataParserManager.register(PROTOCOL_PREFIX + "0.3", DataParserGmmc.class);
	}

	/**
	 * 数据包准许最大容量2M
	 */
	private static final int DISCARDS_MAX_LENGTH = 1024 * 1024 * 2;

	/**
	 * ## 广汽三菱数据包格式,基于国标协议<br>
	 * ### 协议应采用大端模式的网络字节序来传递字和双字 具体内容见协议[广汽三菱车联网-车载终端通信协议及数据]<br>
	 */
	@Override
	public List<DataPack> extract(ByteBuf buffer) {
		DataPack dataPack;
		List<DataPack> dataPackList = new ArrayList<>();

		// 长度大于2M的数据直接抛弃(恶意数据)
		if (DISCARDS_MAX_LENGTH < buffer.readableBytes()) {
			buffer.clear();
		}

		// 遍历
		int offset1;
		while (buffer.isReadable()) {
			offset1 = buffer.readerIndex();
			// 查找协议头标识--->0x23开头
			if (buffer.getByte(offset1) == (byte) 0x23 && buffer.getByte(offset1 + 1) == (byte) 0x23) {
				if (buffer.readableBytes() > GB_LENGTH) {
					// 记录读取的位置
					buffer.markReaderIndex();
					// 获取协议头
					byte[] header = new byte[24];
					// 写入协议头数据
					buffer.readBytes(header);
					// 获取包体长度
					byte[] length = new byte[2];
					System.arraycopy(header, 22, length, 0, 2);

					int bit32 = 0;
					bit32 = length[0] & 0xFF;
					bit32 = bit32 << 8;
					bit32 |= (length[1] & 0xFF);

					if (buffer.readableBytes() < bit32) {
						buffer.resetReaderIndex();
						break;
					}

					// 重置读指针的位置,最终读取的数据需要包含头部信息
					buffer.resetReaderIndex();
					// 数据包总长度为包头24+包体长度+包尾1
					ByteBuf data = buffer.readBytes(bit32 + 25);
					// 创建存储数组
					byte[] dataBytes = new byte[bit32 + 25];
					// 读取数据到数组
					data.readBytes(dataBytes);
					// 数据包校验
					boolean check = false;
					int offset = 0;// 起始位置
					int packetSize = dataBytes.length;// 数据包长度
					/**
					 * 采用BCC（异或校验）法，<br>
					 * 校验范围从命令单元的第一个字节开始，同后一字节异或，<br>
					 * 直到校验码前一字节为止，校验码占用一个字节<br>
					 */
					if (dataBytes[offset] == (byte) 0x23 && dataBytes[offset + 1] == (byte) 0x23) {
						int crc = dataBytes[offset + 4] & 0xFF;
						for (int i = offset + 5; i < offset + packetSize - 1; i++) {
							crc = crc ^ (dataBytes[i] & 0xFF);
						}
						if (crc != (dataBytes[offset + packetSize - 1] & 0xFF)) {
							check = false;
						} else {
							check = true;
						}
					}

					// 打包
					if (check) {
						dataPack = new DataPack(PROTOCOL_GROUP, PROTOCOL_NAME, PROTOCOL_VERSION);
						ByteBuf buf = Unpooled.wrappedBuffer(dataBytes);
						dataPack.setBuf(buf);
						dataPackList.add(dataPack);
					}
				}
			} else {
				// 协议头不符合，跳过这个字节
				buffer.skipBytes(1);
			}
		}
		// 扔掉已读数据
		buffer.discardReadBytes();
		return dataPackList;
	}

	/**
	 * 应答
	 */
	@Override
	public ByteBuf createResponse(DataPack requestPack, ERespReason reason) {
		if (null != requestPack && null != reason) {
			// 原始数据
			byte[] dataPackBytes = Base64.getDecoder().decode(requestPack.getDataB64());
			
			
			
		}
		// TODO
		return null;
	}

	/**
	 * 销毁应答
	 */
	@Override
	public void destroyResponse(ByteBuf responseBuf) {
		// TODO
	}

	/**
	 * 解析完整数据包
	 */
	@Override
	public List<DataPackTarget> extractBody(DataPack dataPack) {
		// TODO
		return null;
	}

	/**
	 * 解析数据包获取vin/设备号/协议
	 */
	@Override
	public Map<String, Object> getMetaData(ByteBuf buffer) {
		// TODO
		return null;
	}
}
