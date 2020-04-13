package com.atguigu.netty.simple;

import io.netty.util.NettyRuntime;

public class TestProcessors {
    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors()); //打印CPU核数
    }
}
