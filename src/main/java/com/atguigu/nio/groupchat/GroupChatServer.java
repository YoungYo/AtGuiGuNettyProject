package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            //得到ServerSocketChannel
            listenChannel = ServerSocketChannel.open();

            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try {
            //循环处理
            while (true){
                int count = selector.select();
                if (count > 0){ //有事件要处理
                    //遍历得到的SelectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出SelectionKey
                        SelectionKey key = iterator.next();

                        //监听到accept事件
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false); //设置非阻塞
                            //将sc注册到selector上
                            sc.register(selector, SelectionKey.OP_READ);
                            //打印提示信息
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }

                        //监听到可读事件
                        if (key.isReadable()){
                            //处理读
                            readData(key);
                        }

                        //手动从集合中移除当前的SelectionKey，防止重复操作。
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待客户端连接……");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    //读取客户端消息
    public void readData(SelectionKey key){
        //定义一个SocketChannel
        SocketChannel channel = null;
        //取到关联的channel
        channel = (SocketChannel) key.channel();
        //创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress remoteAddress = null;
        try {
            remoteAddress = channel.getRemoteAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            int count = channel.read(buffer);
            if (count > 0){
                //把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                //输出客户端消息
                System.out.printf("%s：%s\n", remoteAddress, msg);
                //向其他客户端转发消息
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            System.out.printf("%s 离线\n", remoteAddress);
            key.cancel(); //取消注册
            try {
                channel.close(); //关闭通道
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    //向其他客户端转发消息
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息");
        msg = self.getRemoteAddress() + ": " + msg;
        //遍历所有注册到selector上的SocketChannel，并排除self
        for (SelectionKey key: selector.keys()){
            //通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
