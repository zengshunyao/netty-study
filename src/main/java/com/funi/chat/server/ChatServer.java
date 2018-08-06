package com.funi.chat.server;

import com.funi.chat.server.handler.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class ChatServer {

    private int port = 8080;

    public void start() {
        //Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //启动引擎
            ServerBootstrap b = new ServerBootstrap();
            //主从模式
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    //所有自定义的业务从这里开始

                                    //解码和编码Http请求类
                                    socketChannel.pipeline().addLast(new HttpServerCodec());

                                    socketChannel.pipeline().addLast(new HttpHandler());
                                }
                            }
                    );

            //等待客户端连接
            ChannelFuture future = b.bind(this.port).sync();
            System.out.println("服务已经启动,监听端口：" + this.port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
