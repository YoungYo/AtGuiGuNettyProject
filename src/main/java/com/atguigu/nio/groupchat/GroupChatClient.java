package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    //定义相关的属性
    private final String HOST = "127.0.0.1"; //服务器的IP
    private final int PORT = 6667; //服务器的端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    //构造器，完成初始化工作
    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        //连接服务器
        socketChannel.connect(new InetSocketAddress(HOST, PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将socketChannel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString();
        System.out.println(username + " is OK.");
    }

    //向服务器发送消息
    private void sendInfo(String info){
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务器接收的消息
    private void readInfo(){
        try {
            int readChannel = selector.select();
            if (readChannel > 0){ //有可以用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到一个Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取
                        sc.read(buffer);
                        //把缓冲区的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg);
                    }
                    iterator.remove(); //删除当前的SelectionKey，防止重复操作
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();

        //启动一个线程
        new Thread(){
            public void run(){
                while (true){
                    groupChatClient.readInfo();
                }
            }
        }.start();

        //发送数据给客户端
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()){
            String msg = sc.nextLine();
            groupChatClient.sendInfo(msg);
        }
    }
}
