package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;
import java.util.RandomAccess;

/**
 * MappedByteBuffer 可以让文件直接在内存（堆外内存）中修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");

        //获取对应的文件通道
        FileChannel fileChannel = randomAccessFile.getChannel();

        /**
         * 参数1：指定模式。FileChannel.MapMode.READ_WRITE 表示读写模式
         * 参数2：可以直接修改的起始位置
         * 参数3：映射到内存的大小，单位是字节
         * 该方法实际返回的类型是 DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        randomAccessFile.close();
    }
}
