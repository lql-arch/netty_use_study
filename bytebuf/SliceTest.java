package bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SliceTest {
    private static final Logger log = LogManager.getLogger();
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(9,100);
        ByteBuf buf1 = buf.slice();
        log.debug(buf+"\n"+buf1);
        buf.writeInt(123342);
        log.debug(buf+"\n"+buf1);
    }
}

class Duplicate{
    private static final Logger log = LogManager.getLogger();
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(9,100);
        ByteBuf buf1 = buf.duplicate();
        log.info(buf1.maxCapacity());
        log.debug(buf+"\n"+buf1);
        buf.writeInt(123342);
        log.debug(buf+"\n"+buf1);
    }
}