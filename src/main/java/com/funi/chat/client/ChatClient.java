package com.funi.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatClient {
    private String host;
    private int port;

    public void connect(String host, int port) {
        this.host = host;
        this.port = port;

        EventLoopGroup workrtHroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workrtHroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
//                ch.pipeline().addLast(new );
//                ch.pipeline().addLast(new );
//                ch.pipeline().addLast(new );
//                ch.pipeline().addLast(new );

                }
            });
            ChannelFuture future = b.connect(this.host, this.port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workrtHroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
//        new ChatClient().connect("localhost", 8080);
        System.out.println(Runtime.getRuntime().availableProcessors());
    }


}
