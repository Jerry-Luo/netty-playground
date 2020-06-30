package com.jerry.netty.pojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author <a href="jianwei@outlook.com">Jerry Luo</a>
 * @since 2020/7/1 00:03
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int)m.value());

        // (1)
        // There are quite a few important things in this single line.

        // First, we pass the original ChannelPromise as-is so that Netty marks it as success or failure when the encoded data
        // is actually written out to the wire.

        // Second, we did not call ctx.flush().
        // There is a separate handler method void flush(ChannelHandlerContext ctx) which is purposed to override the flush() operation.
        ctx.write(encoded, promise); // (1)
    }

}
