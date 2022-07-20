package NettyChat_SimpleTest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Decode extends MessageToMessageDecoder<ByteBuf> {
    private static final Logger log = LogManager.getLogger();
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        int length = buf.readableBytes();
        byte[] b = new byte[length];
        buf.getBytes(buf.readerIndex(),b);
        String str = new String(b,0,length,CharsetUtil.UTF_8);
        out.add(str);
    }
}
