package com.funi.chat.server;

import com.funi.chat.server.handler.HttpHandler;
import com.funi.chat.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

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
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel client) throws Exception {
                                    //所有自定义的业务从这里开始

                                    //============这里是用来支持http协议===========
                                    ChannelPipeline pipeline = client.pipeline();
                                    //解码和编码Http请求的类
                                    pipeline.addLast(new HttpServerCodec());

                                    pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                                    pipeline.addLast(new ChunkedWriteHandler());
                                    pipeline.addLast(new HttpHandler());

                                    //============这里是用来支持webSocket协议===========
                                    pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
                                    pipeline.addLast(new WebSocketHandler());
                                }
                            }
                    );

            //等待客户端连接
            ChannelFuture future = b.bind(this.port).sync();
            System.out.println("服务已经启动,监听端口：" + this.port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }
}
