package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建 Boss Group 和 Worker Group
         * 说明：
         * 1. 创建两个线程组，bossGroup 和 workerGroup。
         * 2. bossGroup 只是负责处理连接请求，workerGroup 真正地处理业务逻辑。
         * 3. 两个都是无限循环
         * 4. bossGroup 和 workerGroup 含有的子线程（NioEventLoop）的个数默认是等于CPU核数×2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); //将bossGroup中的子线程个数设置为1
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列等待连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道初始化对象（匿名对象）
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户端对应的SocketChannel的hashcode：" + ch.hashCode()); //可以使用一个集合管理SocketChannel，
                            // 再推送消息时，可以将业务加入到各个Channel对应的NioEventLoop的taskQueue或者scheduleTaskQueue
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); //给我们的workerGroup的EventLoop对应的管道设置处理器
            System.out.println("服务器准备好了");

            //绑定一个端口并且同步。生成了一个ChannelFuture对象。启动服务器并绑定端口
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //给cf注册监听器，监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (cf.isSuccess()){
                        System.out.println("监听端口6668成功");
                    }else {
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
