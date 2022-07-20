package decoderAndEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.List;

public class Byte2IntegerDecoder extends ByteToMessageDecoder {
    private static final Logger log = LogManager.getLogger();

    ByteBuf buf = Unpooled.buffer();
    Charset charset;

    public Byte2IntegerDecoder(){

    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        while(byteBuf.readableBytes() >= 4) {
            int i = byteBuf.readInt();
            log.info(i);
        }

    }
}

class IntegerProcessHandle extends ChannelInboundHandlerAdapter{
    private static final Logger log = LogManager.getLogger();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Integer integer = (Integer)msg;
        super.channelRead(ctx, msg);
    }
}

class tset{
    public void testByteToInteger(){
        ChannelInitializer<EmbeddedChannel> i = new ChannelInitializer<>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new Byte2IntegerDecoder());
                ch.pipeline().addLast(new IntegerProcessHandle());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        for(int j = 0 ; j < 10 ; j++){
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }
    }

    public static void main(String[] args) {
        new tset().testByteToInteger();
    }
}