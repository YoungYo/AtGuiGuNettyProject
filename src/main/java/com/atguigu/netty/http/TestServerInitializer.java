package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty提供的httpServerCodec（Codec 是 coder和decoder的缩写）
        //HttpServerCodec是netty提供的处理Http的编解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

        //增加一个自定义的Handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}
