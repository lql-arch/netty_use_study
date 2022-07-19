package bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BufferTypeTest {
    private static final Logger log = LogManager.getLogger();
    final static Charset UTF_8 = StandardCharsets.UTF_8;
    //堆缓冲区测试用例

    public void testHeapBuffer() {
//取得堆内存
        ByteBuf heapBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        heapBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));

        if (heapBuf.hasArray()) {
//取得内部数组
//            byte[] array = heapBuf.array();
//            int offset = heapBuf.arrayOffset() +
//                    heapBuf.readerIndex();
//            int length = heapBuf.readableBytes();
            //log.info(heapBuf+":"+new String(array,offset,length, UTF_8));
            log.info(heapBuf+":"+new String(heapBuf.array(),heapBuf.readerIndex(),heapBuf.writerIndex(),UTF_8));
        }
        heapBuf.release();
    }
    //直接缓冲区测试用例
    public void testDirectBuffer() {
        ByteBuf directBuf= ByteBufAllocator.DEFAULT.directBuffer();
        //log.info("after retain:"+directBuf.refCnt());
        directBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));

        //hasArray()返回false,不一定代表缓冲区一定就是Direct ByteBuf,
        //也有可能是CompositeByteBuf。
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
//把数据读取到堆内存array中,再进行Java处理
            directBuf.getBytes(directBuf.readerIndex(), array);
            log.info(directBuf+":"+new String(array, UTF_8));
        }
        directBuf.release();
        //log.info("after retain:"+directBuf.refCnt());
    }

    public static void main(String[] args) {
        new BufferTypeTest().testDirectBuffer();
        new BufferTypeTest().testHeapBuffer();
        //创建堆缓冲区
        ByteBuf heapBuf = Unpooled.buffer(8);
//创建直接缓冲区
        ByteBuf directBuf = Unpooled.directBuffer(16);
//创建复合缓冲区
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        log.info("\n"+heapBuf+"\n"+directBuf+"\n"+compBuf);
    }
}
