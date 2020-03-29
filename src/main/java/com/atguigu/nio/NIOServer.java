package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把ServerSocketChannel注册到Selector，关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true){

            //等待1秒，如果没有事件发生，就继续循环
            if (selector.select(1000) == 0){ //没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            //如果返回的大于0，就获取到相关的SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //使用迭代遍历器遍历selectionKeys
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                //获取到SelectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的时间做相应的处理
                if (key.isAcceptable()){ //如果是OP_ACCEPT事件，说明有新的客户端连接我
                    //给该客户端生成一个SocketChanel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()){ //发生了OP_READ
                    //通过key反向获取到对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel关联的Buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("From 客户端：" + new String(buffer.array()));
                }

                //手动从集合中移除当前的SelectionKey，防止重复操作。
                keyIterator.remove();
            }
        }
    }
}
