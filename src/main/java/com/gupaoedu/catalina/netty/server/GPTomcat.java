package com.gupaoedu.catalina.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.IOException;

public class GPTomcat {

    public void start(int port) throws IOException, InterruptedException {

//        ServerSocketChannel server=ServerSocketChannel.open();
//        server.bind(new InetSocketAddress("localhost",port));

//        ServerSocket server=new ServerSocket(port);

        //主从
//Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //Worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //Netty服务
        ServerBootstrap server = new ServerBootstrap();
        try {
            server.group(bossGroup, workerGroup)
                    //主线程处理类
                    .channel(NioServerSocketChannel.class)
                    //子线程的处理,Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {

                            //无锁化串行编程
                            ChannelPipeline pipeline = client.pipeline();
                            //业务逻辑链路，编码器
                            pipeline.addLast(new HttpResponseEncoder());

                            //解码器
                            pipeline.addLast(new HttpRequestDecoder());

                            //业务逻辑处理
                            pipeline.addLast(new GPTomcatHandler());
                        }
                    })
                    //配置信息
                    //针对主线程配置
                    .option(ChannelOption.SO_BACKLOG, 128/*线程数量*/)
                    //针对子线程配置
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //同步 阻塞
            ChannelFuture future = server.bind(port).sync();
            System.out.println("GPTomcat已经启动" + port);

            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


    public static void main(String[] args) {

        try {
            new GPTomcat().start(8080);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
