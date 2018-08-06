package com.gupaoedu.catalina.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;


public class GPResponse {
    private ChannelHandlerContext context;

    private HttpRequest request;

    public GPResponse(ChannelHandlerContext ctx, HttpRequest request) {
        this.context = ctx;
        this.request = request;
    }

    public void write(String out) throws UnsupportedEncodingException {
        try {


            if (out == null) {
                return;
            }
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK
                            ,
                            Unpooled.wrappedBuffer(out.getBytes("UTF-8")));

            // HttpHeaders headers = new DefaultHttpHeaders();

//        response.headers().set(headers);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaderNames.EXPIRES, 0);
            if (HttpHeaders.isKeepAlive(request)) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }

            context.write(response);
        } finally {
            context.flush();
        }
    }
}
