package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("f:\\file01.txt");
        FileInputStream fis = new FileInputStream(file);

        //通过fis获取对应的FileChannel
        FileChannel fileChannel = fis.getChannel();//这个fileChannel的真实类型是FileChannelImpl


        //创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据读入到byteBuffer
        fileChannel.read(byteBuffer);

        //将byteBuffer的字节数据转成字符串
        System.out.println(new String(byteBuffer.array()));
        fis.close();
    }
}
