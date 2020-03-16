package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("f:\\file01.txt");
        FileChannel fileInputChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("f:\\file02.txt");
        FileChannel fileOutputChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true){
            //清空byteBuffer
            byteBuffer.clear();//这是一个重要的操作，一定不要忘了
            int read = fileInputChannel.read(byteBuffer);
            if (read == -1){
                break;
            }
            byteBuffer.flip();
            fileOutputChannel.write(byteBuffer);
        }
    }
}
