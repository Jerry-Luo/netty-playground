package com.jerry.netty.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * time client
 *
 * @author <a href="jianwei@outlook.com">Jerry Luo</a>
 * @since 2020/6/30 22:49
 */
public class TimeClient {
    public static void main(String[] args) throws Exception {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // (1)
            // Bootstrap is similar to ServerBootstrap except that it's for non-server channels such as a client-side or connectionless channel.
            Bootstrap b = new Bootstrap(); // (1)

            // (2)
            // If you specify only one EventLoopGroup, it will be used both as a boss group and as a worker group.
            // The boss worker is not used for the client side though.
            b.group(workerGroup); // (2)

            // (3)
            // Instead of NioServerSocketChannel, NioSocketChannel is being used to create a client-side Channel.
            b.channel(NioSocketChannel.class); // (3)

            // (4)
            // Note that we do not use childOption() here unlike we did with ServerBootstrap
            // because the client-side SocketChannel does not have a parent.
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    // However, this handler sometimes will refuse to work raising an IndexOutOfBoundsException.
                    // ch.pipeline().addLast(new TimeClientHandler());

                    // 解决方案一 自己在 ChannelInboundHandler 中处理，会增加单一 ChannelInboundHandler 的复杂度
                    // ch.pipeline().addLast(new TimeClientHandlerSolution1());

                    // 解决方案二 分开处理 一个ChannelInboundHandler专门处理数据编排 一个ChannelInboundHandler专门处理业务 逻辑清晰
                    // ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());

                    // 方案三 使用 ReplayingDecoder 进一步简化处理逻辑
                    ch.pipeline().addLast(new TimeDecoder1(), new TimeClientHandler());

                    // Additionally, Netty provides out-of-the-box decoders which enables you to implement most protocols very easily
                    // and helps you avoid from ending up with a monolithic unmaintainable handler implementation.

                    // Please refer to the following packages for more detailed examples:
                    //io.netty.example.factorial for a binary protocol, and
                    //io.netty.example.telnet for a text line-based protocol.
                }
            });

            // Start the client.
            // (5)
            // We should call the connect() method instead of the bind() method.
            ChannelFuture f = b.connect("localhost", 8082).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
