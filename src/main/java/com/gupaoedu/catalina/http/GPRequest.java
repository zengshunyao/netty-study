package com.gupaoedu.catalina.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class GPRequest {


    private ChannelHandlerContext context;

    private HttpRequest request;

    public GPRequest(ChannelHandlerContext ctx, HttpRequest request) {

        this.context = ctx;
        this.request = request;
    }

    public String getURi() {
        return request.getUri();
    }

    public String getMethod() {
        return request.getMethod().name();
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
        return decoder.parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> params = this.getParameters();
        if (params == null || params.get(name) == null) {
            return null;
        } else {
            return params.get(name).get(0);
        }
    }
}
