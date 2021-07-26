package com.yang;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.Test;

/**
 * <p>@ProjectName:stady-nio</p>
 * <p>@Package:com.yang</p>
 * <p>@ClassName:TestBlockingNio2</p>
 * <p>@Description:${description}</p>
 * <p>@Author:yang</p>
 * <p>@Date:2021/2/5 10:45</p>
 * <p>@Version:1.0</p>
 */
public class TestBlockingNio2 {

    @Test
    public void client() {

        SocketChannel socketChannel = null;
        FileChannel fileChannel = null;
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

            fileChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while (fileChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }

            socketChannel.shutdownOutput();

            //接受服务的反馈
            int len = 0;
            while ((len = socketChannel.read(byteBuffer)) != -1) {
                byteBuffer.flip();
                System.out.println(new String(byteBuffer.array(), 0, len));
                byteBuffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketChannel != null) {
                    socketChannel.close();
                }
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void server() {

        ServerSocketChannel serverSocketChannel = null;
        FileChannel fileChannel = null;
        SocketChannel socketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();

            fileChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            serverSocketChannel.bind(new InetSocketAddress(9898));

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            socketChannel = serverSocketChannel.accept();

            while (socketChannel.read(byteBuffer) != -1) {
                byteBuffer.flip();
                fileChannel.write(byteBuffer);
                byteBuffer.clear();
            }

            //发送反馈
            byteBuffer.put("接受成功".getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocketChannel != null) {
                    serverSocketChannel.close();
                }
                if (socketChannel != null) {
                    socketChannel.close();
                }
                if (fileChannel != null) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
