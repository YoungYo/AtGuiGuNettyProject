package com.atguigu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        //思路
        //1. 创建一个线程池
        //2. 如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while (true){
            //监听，等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个线程，与客户端通讯
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    //编写一个Handler方法，和客户端通讯
    public static void handler(Socket socket){
        System.out.printf("线程信息：id=%d, name=%s\n", Thread.currentThread().getId(), Thread.currentThread().getName());
        try {
            byte[] bytes = new byte[1024];
            //通过Socket获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while(true){
                System.out.printf("线程信息：id=%d, name=%s\n", Thread.currentThread().getId(), Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes, 0, read)); //输出客户端发送的数据
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
