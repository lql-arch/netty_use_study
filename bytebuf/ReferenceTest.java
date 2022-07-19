package bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReferenceTest {
    private static final Logger log = LogManager.getLogger();

    public void testRef() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        log.info("after create:" + buffer.refCnt());
        buffer.retain();
        //增加一次引用计数
        log.info("after retain:" + buffer.refCnt());
        buffer.release();
        //减少一次引用计数
        log.info("after release:" + buffer.refCnt());
        buffer.release();
        //减少一次引用计数
        log.info("after release:" + buffer.refCnt());
        //错误:refCnt: 0,不能再retain
        buffer.retain();
        //增加一次引用计数
        log.info("after retain:" + buffer.refCnt());
    }

    public static void main(String[] args) {
        new ReferenceTest().testRef();
    }


}
