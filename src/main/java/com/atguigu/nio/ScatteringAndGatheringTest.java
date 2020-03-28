package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering（分散）：将数据写入到Buffer时，可以采用Buffer数组依次写入
 * Gathering：从Buffer读取数据时，可以采用Buffer数组依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        //使用ServerSocketChannel和SocketChanel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到Socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建Buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //监听。等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8; //假定从客户端接受8个字节
        //循环读取
        while (true){
            int byteRead = 0;
            while (byteRead < messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l; // 累计读取的字节数
                System.out.println("byteRead=" + byteRead);
                //使用流打印，看看当前Buffer的position和limit
                Arrays.stream(byteBuffers).map(buffer -> "position=" + buffer.position() + ", limit=" + buffer.limit()).forEach(System.out::println);
            }
            //将所有的Buffer进行反转
            Arrays.asList(byteBuffers).forEach(ByteBuffer::flip);

            //将数据读出，显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength){
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            //将所有的Buffer清空
            Arrays.asList(byteBuffers).forEach(ByteBuffer::clear);

            System.out.printf("byteRead=%d, byteWrite=%d, messageLength=%d\n", byteRead, byteWrite, messageLength);
        }
    }
}
