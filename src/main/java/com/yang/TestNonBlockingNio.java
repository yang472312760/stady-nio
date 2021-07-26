package com.yang;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import org.junit.Test;

/**
 * <p>@ProjectName:stady-nio</p>
 * <p>@Package:com.yang</p>
 * <p>@ClassName:TestNonBlockingNio</p>
 * <p>@Description:${description}</p>
 * <p>@Author:yang</p>
 * <p>@Date:2021/2/5 11:55</p>
 * <p>@Version:1.0</p>
 */
public class TestNonBlockingNio {

    @Test
    public void client() {

        SocketChannel socketChannel = null;
        try {
            //获取通道
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            //切换非阻塞式
            socketChannel.configureBlocking(false);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            byteBuffer.put(new Date().toString().getBytes());
            byteBuffer.flip();

            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketChannel != null) {
                    socketChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void server() {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        try {
            //获取通道
            serverSocketChannel = ServerSocketChannel.open();

            //切换非阻塞方式
            serverSocketChannel.configureBlocking(false);

            //绑定连接
            serverSocketChannel.bind(new InetSocketAddress(9898));

            //获取选择器
            Selector selector = Selector.open();

            //将通道注册到选择器，并且指定监听事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //轮询获取选择器上准备就绪的事件
            while (selector.select() > 0) {
                //获取当前选择器中所有注册的选择健（已就绪的监听事件）
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    //获取准备就绪的时间
                    SelectionKey key = keys.next();
                    //判断具体是什么事件准备就绪
                    if (key.isAcceptable()) {
                        //若接收选择器，获取客户端连接
                        socketChannel = serverSocketChannel.accept();
                        //切换非阻塞模式
                        socketChannel.configureBlocking(false);

                        socketChannel.register(selector, SelectionKey.OP_READ);

                    } else if (key.isReadable()) {

                        //获取当前选择器上读就绪状态的通道
                        socketChannel = (SocketChannel) key.channel();

                        //读取数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = socketChannel.read(byteBuffer)) > -1) {
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array(), 0, len));
                            byteBuffer.clear();
                        }
                    }
                    keys.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

}
