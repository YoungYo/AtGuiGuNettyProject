package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //通道就绪时会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("client: %s\n", ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server! 喵～", CharsetUtil.UTF_8));
    }

    //当通道有可读事件时会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将msg转成一个ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.printf("服务器回复的消息：%s\n", buf.toString(CharsetUtil.UTF_8));
        System.out.printf("服务器的地址：%s\n", ctx.channel().remoteAddress());
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
