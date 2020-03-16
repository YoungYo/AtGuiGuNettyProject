package com.atguigu.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello，尚硅谷";
        //创建一个输出流
        FileOutputStream fos = new FileOutputStream("f:\\file01.txt");

        //通过输出流获取对应的FileChannel
        FileChannel fileChannel = fos.getChannel(); //这个fileChannel的真实类型是FileChannelImpl

        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将str放进byteBuffer
        byteBuffer.put(str.getBytes());

        //对byteBuffer进行反转
        byteBuffer.flip();

        //将byteBuffer数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fos.close();
    }
}
