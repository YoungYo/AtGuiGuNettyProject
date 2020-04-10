package com.atguigu.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        String filename = "woman.jpg";
        //得到一个文件Channel
        FileChannel fileChannel = new FileInputStream(filename).getChannel();
        //准备发送
        long startTime = System.currentTimeMillis();

        //在Linux下调用一次transferTo方法就可以完成文件传输
        //在Windows下调用一次transferTo方法只能发送8M大小的文件，所以在Windows下要分段传输文件，而且要注意传输时的位置
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel); //transferTo底层就使用了零拷贝
        System.out.printf("发送的总的字节数：%d, 耗时：%d\n", transferCount, System.currentTimeMillis() - startTime);

        //关闭通道
        fileChannel.close();
    }
}
