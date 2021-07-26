package com.yang;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.junit.Test;

/**
 * 一、缓冲区（Buffer）：在Java NIO中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
 * <p>
 * 根据不同数据类型不同（boolean除外），提供了相应类型的缓冲区:
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 * 上述缓冲区的管理方式几乎一致，通过allocate()获取缓冲区
 */
public class TestBuffer {

    @Test
    public void test5() throws CharacterCodingException {


        Charset gbk = Charset.forName("GBK");
        //获取编码器
        CharsetEncoder encoder = gbk.newEncoder();
        //获取解码器
        CharsetDecoder decoder = gbk.newDecoder();

        CharBuffer charBuffer = CharBuffer.allocate(1024);


        CharBuffer cb = charBuffer.put("杨成林");

        cb.flip();

        //编码
        ByteBuffer byteBuffer = encoder.encode(cb);
        for (int i = 0; i < 6; i++) {
            System.out.println(byteBuffer.get());
        }
        byteBuffer.flip();
        //解码
        CharBuffer charBuffer1 = decoder.decode(byteBuffer);
        System.out.println(charBuffer1.toString());

    }

    @Test
    public void test4() {

        SortedMap<String, Charset> map = Charset.availableCharsets();

        Set<Map.Entry<String, Charset>> set = map.entrySet();

        for (Map.Entry<String, Charset> entry : set) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

    }

    @Test
    public void test3() {

        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            /**
             * 分散读
             */
            RandomAccessFile accessFile = new RandomAccessFile("1.txt", "rw");
            //获取通道
            inChannel = accessFile.getChannel();
            //分配指定大小的缓冲区
            ByteBuffer buffer1 = ByteBuffer.allocate(100);
            ByteBuffer buffer2 = ByteBuffer.allocate(1024);

            ByteBuffer[] byteBuffers = { buffer1, buffer2 };

            inChannel.read(byteBuffers);

            for (ByteBuffer byteBuffer : byteBuffers) {
                byteBuffer.flip();
            }

            System.out.println(new String(byteBuffers[0].array(), 0, byteBuffers[0].limit()));
            System.out.println(new String(byteBuffers[1].array(), 0, byteBuffers[1].limit()));

            /**
             * 聚集写入
             */
            RandomAccessFile file = new RandomAccessFile("2.txt", "rw");
            outChannel = file.getChannel();
            outChannel.write(byteBuffers);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void test2() {
        String str = "abcde";

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(str.getBytes());

        byteBuffer.flip();

        byte[] b = new byte[byteBuffer.limit()];
        byteBuffer.get(b, 0, 2);
        System.out.println(new String(b, 0, 2));
        System.out.println(byteBuffer.position());

        //标记
        byteBuffer.mark();
        byteBuffer.get(b, 2, 2);
        System.out.println(new String(b, 2, 2));
        System.out.println(byteBuffer.position());

        byteBuffer.reset();
        System.out.println(byteBuffer.position());

    }

    @Test
    public void test1() {

        String str = "abcde";
        //分配一个指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        System.out.println("===================allocate========================");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //存入数据到缓冲区
        byteBuffer.put(str.getBytes());
        System.out.println("======================put=====================");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //切换成读取数据模式
        byteBuffer.flip();
        System.out.println("=======================flip====================");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //利用get()读取数据
        System.out.println("=====================get======================");
        byte[] b = new byte[byteBuffer.limit()];
        byteBuffer.get(b);
        System.out.println(new String(b, 0, b.length));
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //可重复读数据
        byteBuffer.rewind();
        System.out.println("=====================rewind======================");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //清空缓冲区，但是缓冲区的数据依然存在，但是处于“被遗忘”状态
        byteBuffer.clear();
        System.out.println("=====================clear======================");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
    }

}
