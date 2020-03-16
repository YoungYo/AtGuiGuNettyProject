package com.atguigu.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //举例说明Buffer的使用
        //创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //想Buffer中存放数据
//        intBuffer.put(10);
//        intBuffer.put(11);
//        intBuffer.put(12);
//        intBuffer.put(13);
//        intBuffer.put(14);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        //如何从Buffer读取数据
        intBuffer.flip(); //讲Buffer转换，读写切换

        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
