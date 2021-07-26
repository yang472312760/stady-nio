package com.yang;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import org.junit.Test;

/**
 * <p>@ProjectName:stady-nio</p>
 * <p>@Package:com.yang</p>
 * <p>@ClassName:TestPipe</p>
 * <p>@Description:${description}</p>
 * <p>@Author:yang</p>
 * <p>@Date:2021/2/5 15:05</p>
 * <p>@Version:1.0</p>
 */
public class TestPipe {

    @Test
    public void testPipe() throws IOException {

        Pipe pipe = Pipe.open();

        Pipe.SinkChannel sinkChannel = pipe.sink();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put("发送数据".getBytes());

        byteBuffer.flip();

        sinkChannel.write(byteBuffer);

        Pipe.SourceChannel sourceChannel = pipe.source();
        byteBuffer.flip();
        int read = sourceChannel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array(), 0, read));

        sinkChannel.close();
        sourceChannel.close();

    }

}
