package com.gupaoedu.catalina.netty.server;

import com.gupaoedu.catalina.http.GPRequest;
import com.gupaoedu.catalina.http.GPResponse;
import com.gupaoedu.catalina.servlets.MyServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

public class GPTomcatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        if (msg instanceof HttpRequest) {
            HttpRequest req = ((HttpRequest) msg);

            GPRequest request = new GPRequest(ctx, req);
            GPResponse response = new GPResponse(ctx, req);

            MyServlet servlet = MyServlet.class.newInstance();

            servlet.doGet(request, response);
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
