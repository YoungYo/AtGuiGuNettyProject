package com.atguigu.nio;

import java.io.*;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws IOException {
        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("f:\\a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("f:\\b.jpg");

        //获取各个流对应的FileChannel
        FileChannel srcChannel = fileInputStream.getChannel();
        FileChannel dstChannel = fileOutputStream.getChannel();

        //使用transFrom完成拷贝
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

        //关闭相关通道和流
        srcChannel.close();
        dstChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
