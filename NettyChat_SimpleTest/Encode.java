package NettyChat_SimpleTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Encode extends MessageToMessageEncoder<String> {//编码
    private static final Logger log = LogManager.getLogger();

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();
        buf.writeBytes(msg.getBytes(CharsetUtil.UTF_8));
        out.add(buf);
    }
}
