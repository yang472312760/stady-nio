package com.yang;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import org.junit.Test;

/**
 * <p>@ProjectName:stady-nio</p>
 * <p>@Package:com.yang</p>
 * <p>@ClassName:TestNonBlockingNio2</p>
 * <p>@Description:${description}</p>
 * <p>@Author:yang</p>
 * <p>@Date:2021/2/5 14:11</p>
 * <p>@Version:1.0</p>
 */
public class TestNonBlockingNio2 {

    @Test
    public void send() throws IOException {

        DatagramChannel datagramChannel = DatagramChannel.open();

        datagramChannel.configureBlocking(false);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String next = scanner.next();
            byteBuffer.put((new Date().toString() + "\t" + next).getBytes());
            byteBuffer.flip();
            datagramChannel.send(byteBuffer, new InetSocketAddress("127.0.0.1", 9898));
            byteBuffer.clear();
        }

        datagramChannel.close();
    }

    @Test
    public void receive() {
        DatagramChannel datagramChannel = null;
        try {
            datagramChannel = DatagramChannel.open();

            datagramChannel.configureBlocking(false);

            datagramChannel.bind(new InetSocketAddress(9898));

            Selector selector = Selector.open();

            datagramChannel.register(selector, SelectionKey.OP_READ);

            while (selector.select() > 0) {
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if (key.isReadable()) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        datagramChannel.receive(byteBuffer);
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(), 0, byteBuffer.limit()));
                        byteBuffer.clear();
                    }
                }
                keys.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (datagramChannel != null) {
                    datagramChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
