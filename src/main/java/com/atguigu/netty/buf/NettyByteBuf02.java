package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello,world!", Charset.forName("utf-8"));

        //使用相关的方法
        if (byteBuf.hasArray()){
            byte[] content = byteBuf.array();
            //将content转成字符串
            System.out.println(new String(content, Charset.forName("utf-8")));
            System.out.println("byteBuf=" + byteBuf);
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());
            System.out.println(byteBuf.readableBytes()); //输出可读的字节数

            //使用for循环取出各个字节
            for (int i = 0; i < byteBuf.readableBytes(); i++) {
                System.out.println((char) byteBuf.getByte(i));
            }

            //按照某个范围读取
            System.out.println(byteBuf.getCharSequence(0, 4, Charset.forName("utf-8")));
        }
    }
}
