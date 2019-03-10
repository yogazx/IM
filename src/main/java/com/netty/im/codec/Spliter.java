package com.netty.im.codec;

import com.netty.im.protocol.PacketCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 自定义基于长度域的解包器
 */
public class Spliter extends LengthFieldBasedFrameDecoder {

    /**
     * customize communication protocol
     *  +--------------+-----------+------------------------+-----------+--------------+------------------+
     *  | magic number |  version  |  serializer algorithm  |  command  |  data length |        data      |
     *  |    4 bit     |   1 bit   |          1 bit         |   1 bit   |    4 bit     |        N bit     |
     *  +--------------+-----------+------------------------+-----------+--------------+------------------+
     */
    private static final int LENGTH_FIELD_OFFSET = 7;
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    /**
     * 判断是否是指定协议
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.getInt(in.readerIndex()) != PacketCode.MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
