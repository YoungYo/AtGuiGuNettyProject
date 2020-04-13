package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 说明：
 * 1. 我们自定义一个Handler，需要继承Netty规定好的某个Handler适配器
 * 2. 这时我们自定义的Handler才能成为一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据（这里我们可以读取客户端发送的消息）
     * @param ctx 是一个上下文对象，含有管道（pipeline）和通道（channel）
     * @param msg 是客户端发送的数据，默认是Object类型
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.printf("Server ctx = %s\n", ctx);
        //将msg转成一个ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.printf("客户端发送的消息是：%s\n", buf.toString(CharsetUtil.UTF_8));
        System.out.printf("客户端的地址是：%s\n", ctx.channel().remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存并刷新。一般来讲，需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端～", CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
