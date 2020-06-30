package com.jerry.netty.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * time decoder
 *
 * @author <a href="jianwei@outlook.com">Jerry Luo</a>
 * @since 2020/6/30 23:25
 */
public class TimePojoDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            return;
        }
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
