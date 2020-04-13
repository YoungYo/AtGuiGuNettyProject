package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

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
        //假如这里有一个非常耗时的任务，就需要把任务执行改成异步的
//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("msg2: hello, 客户端～", CharsetUtil.UTF_8));
        //解决方案1：用户程序自定义的普通任务。把任务提交到该Channel对应的NioEventLoop的taskQueue中
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("msg2: hello, 客户端～", CharsetUtil.UTF_8));
            }
        });
        //解决方案2：用户自定义定时任务。把任务提交到该Channel对应的NioEventLoop的scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("msg-schedule: hello, 客户端～", CharsetUtil.UTF_8));
            }
        }, 5, TimeUnit.SECONDS);
        System.out.println("继续执行后面的代码");

/*
        System.out.println("服务器读取线程：" + Thread.currentThread().getName());
        System.out.printf("Server ctx = %s\n", ctx);
        System.out.println("channel 和 pipeline的关系：");
        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); //本质上是一个双向链表，涉及到出栈入栈的问题
        //将msg转成一个ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.printf("客户端发送的消息：%s\n", buf.toString(CharsetUtil.UTF_8));
        System.out.printf("客户端的地址是：%s\n", channel.remoteAddress());
*/
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存并刷新。一般来讲，需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("msg1: hello, 客户端～", CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
