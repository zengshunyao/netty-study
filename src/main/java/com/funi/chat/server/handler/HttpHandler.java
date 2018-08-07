package com.funi.chat.server.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    //classPath
    private URL baseURL = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();

    private final String WEB_ROOT = "webapp";

    private File getFileFromRoot(String fileName) throws URISyntaxException {
        String path = baseURL.toURI().getPath() + WEB_ROOT + "/" + fileName;
        path = path.startsWith("/") ? path.substring(1) : path;
        path.replaceAll("//", "/");

        path = "E:\\ZSY\\IdeaProjects\\1\\netty-study\\target\\netty-study\\" + fileName;
        return new File(path);
    }

    //read0
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //获取到了客户端请求的uri
        String uri = request.getUri();

        String page = uri.equals("/") ? "chat.html" : uri;
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(getFileFromRoot(page), "r");
        } catch (Exception e) {
            ctx.fireChannelRead(request.retain());
        }

        String contentType = "text/html;";

        if (uri.endsWith(".css")) {
            contentType = "text/css;";
        } else if (uri.endsWith(".js")) {
            contentType = "text/javascript;";
        } else if (uri.toLowerCase().matches("(jps|gif|png)$")) {
            String ext = uri.substring(uri.lastIndexOf(".") + 1);
            contentType = "image/" + ext + ";";
        }
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType + "charset=utf-8");
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());

        }

        ctx.write(response);

        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));

//        ctx.flush();

        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        if (keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        file.close();

//        System.out.println(file);
    }
}
