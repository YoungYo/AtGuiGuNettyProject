package com.atguigu.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个Channel组，管理所有的Channel
    //GlobalEventExecutor.INSTANCE是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //handlerAdded表示连接建立，一旦连接建立，该方法第一个被执行。
    //将当前Channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端。该方法会遍历channelGroup中的所有Channel，并发送消息，因此我们不需要自己遍历。
        channelGroup.writeAndFlush("【客户端 " + channel.remoteAddress() + "】加入聊天");
        channelGroup.add(channel);
    }

    //断开连接。将xx离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("【客户端" + channel.remoteAddress() + "】离开了");
        System.out.println("channelGroup size" + channelGroup.size());
    }

    //表示Channel处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("【客户端" + ctx.channel().remoteAddress() + "】上线");
    }

    //表示Channel处于非活动状态，提示xx离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("【客户端" + ctx.channel().remoteAddress() + "】离线");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到当前Channel
        Channel channel = ctx.channel();
        //这是我们遍历channelGroup，根据不同的情况回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch){ //如果ch不是当前的channel，则转发消息
                ch.writeAndFlush("【客户端" + channel.remoteAddress() + "】发送消息：" + msg + "\n");
            }else {
                ch.writeAndFlush("【自己】发送消息：" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
