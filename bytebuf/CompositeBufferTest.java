package bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CompositeBufferTest {
    static Charset utf8 = StandardCharsets.UTF_8;

    public void byteBufComposite() {
        CompositeByteBuf cbuf = ByteBufAllocator.DEFAULT.compositeBuffer();
//消息头
        ByteBuf headerBuf = Unpooled.copiedBuffer("疯狂创客圈:", utf8);
//消息体1
        ByteBuf bodyBuf = Unpooled.copiedBuffer("高性能Netty", utf8);
        cbuf.addComponents(headerBuf, bodyBuf);
        sendMsg(cbuf);
//在refCnt为0前, retain
        headerBuf.retain();
        cbuf.release();
        cbuf = ByteBufAllocator.DEFAULT.compositeBuffer();
//消息体2
        bodyBuf = Unpooled.copiedBuffer("高性能学习社群", utf8);
        cbuf.addComponents(headerBuf, bodyBuf);
        sendMsg(cbuf);
        cbuf.release();
    }
    private void sendMsg(@NotNull CompositeByteBuf cbuf) {
//处理整个消息
        for (ByteBuf b :cbuf) {
            int length = b.readableBytes();
            byte[] array = new byte[length];
//将CompositeByteBuf中的数据统一复制到数组中
            b.getBytes(b.readerIndex(), array);
//处理一下数组中的数据
            System.out.print(new String(array, utf8));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new CompositeBufferTest().byteBufComposite();
    }
}


class NIOCompositeBufferTest {
    public void intCompositeBufComposite() {
        CompositeByteBuf cbuf = Unpooled.compositeBuffer(3);
        cbuf.addComponent(Unpooled.wrappedBuffer("122".getBytes(CharsetUtil.UTF_8)));
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{97}));
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{65, 48,66}));
//合并成一个的Java NIO缓冲区
        ByteBuffer nioBuffer = cbuf.nioBuffer(0,6);
        byte[] bytes = nioBuffer.array();
        System.out.print("bytes = ");
//        for (byte b : bytes) {
//            System.out.print(b);
//        }
        System.out.println(new String(bytes,CharsetUtil.UTF_8));
        cbuf.release();
    }

    public static void main(String[] args) {
        new NIOCompositeBufferTest().intCompositeBufComposite();
    }
}
