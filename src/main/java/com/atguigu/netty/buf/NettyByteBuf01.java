package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        //说明：
        // 1. buffer方法返回的是一个ByteBuf的对象，该对象里面包含一个byte类型的数组
        // 2. 在Netty的ByteBuf中，不需要使用flip进行反转，因为它底层维护了readerIndex和writerIndex
        // 3. 通过readerIndex、writerIndex和capacity，将数组分成了三个部分，
        //    0-readerIndex是已经读取的部分
        //    readerIndex-writerIndex是可读的区域
        //    writerIndex-capacity是可写的区域
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        System.out.println("Capacity=" + buffer.capacity());
        //输出
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
    }
}
