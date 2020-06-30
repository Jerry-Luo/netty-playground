package com.jerry.netty.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * echo server handler
 *
 * @author <a href="jianwei@outlook.com">Jerry Luo</a>
 * @since 2020/6/30 22:15
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // (1)
        // A ChannelHandlerContext object provides various operations that enable you to trigger various I/O events and operations.
        // Here, we invoke write(Object) to write the received message in verbatim.
        // Please note that we did not release the received message unlike we did in the DISCARD example.
        // It is because Netty releases it for you when it is written out to the wire.
        ctx.write(msg); // (1)

        // (2)
        // ctx.write(Object) does not make the message written out to the wire.
        // It is buffered internally and then flushed out to the wire by ctx.flush().
        // Alternatively, you could call ctx.writeAndFlush(msg) for brevity.
        ctx.flush(); // (2)
    }

}
