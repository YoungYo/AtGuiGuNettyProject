package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 说明：
 * 1. SimpleChannelInboundHandler 继承了ChannelInboundHandlerAdapter
 * 2. HttpObject：Http客户端和Http服务器相互通讯的数据被封装成了HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是HttpRequest请求
        if (msg instanceof HttpRequest){
            System.out.println("msg 类型：" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());
            System.out.println("channel hashcode：" + ctx.channel().hashCode());

            //获取到HttpRequest
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取uri，过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico，不做响应");
                return;
            }

            //回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("Hello! 我是服务器。", CharsetUtil.UTF_16);
            //构造一个Http响应，即HttpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
