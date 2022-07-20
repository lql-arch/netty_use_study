package decoderAndEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

class LengthFieldDecoderTest{
    public static void main(String[] args) {
        ChannelInitializer<EmbeddedChannel> ci = new ChannelInitializer<>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,4,8))
                        .addLast(new LoggingHandler());
            }
        };
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(ci);
        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();
        send(buf,"Hello World");
        send(buf,"Hi");
        send(buf,"Hello");
        embeddedChannel.writeInbound(buf);

    }

    private static void send(ByteBuf buf,String content) {
        byte b[] = content.getBytes(CharsetUtil.UTF_8);
        int length = b.length;
        buf.writeInt(length);
        buf.writeInt(1);
        buf.writeBytes(b);
    }

}