package bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WriteReadTest {
    private static final Logger log = LogManager.getLogger();

    public void testWriteRead() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9,
                100);
        log.info("动作:分配ByteBuf(9, 100):"+ buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        log.info("动作:写入4个字节 (1,2,3,4):"+buffer);
        log.info("start==========:get==========");
        getByteBuf(buffer);
        log.info("动作:取数据ByteBuf:"+ buffer);
        log.info("start==========:read==========");
        readByteBuf(buffer);
        log.info("动作:读完ByteBuf:"+ buffer);
    }

    //取字节
    private void readByteBuf(ByteBuf buffer) {
        while (buffer.isReadable()) {
            log.info("取一个字节:" + buffer.readByte());
        }
    }

    //读字节,不改变指针
    private void getByteBuf(ByteBuf buffer) {
        for (int i = 0; i < buffer.readableBytes(); i++) {
            log.info("读一个字节:" + buffer.getByte(i));
        }
    }

    public static void main(String[] args) {
        new WriteReadTest().testWriteRead();
    }
}
