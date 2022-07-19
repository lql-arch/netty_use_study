package bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.AbstractNioByteChannel;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AllocatorTest {
    private static final Logger log = LogManager.getLogger();
    //辅助的方法:输出ByteBuf是否为直接内存,以及内存分配器
    public static void printByteBuf(String action, ByteBuf b) {
        log.info(" ===========" + action + "============");
//true表示缓冲区为Java堆内存(组合缓冲例外)
//false表示缓冲区为操作系统管理的内存(组合缓冲例外)
        log.info("b.hasArray: " + b.hasArray());
//输出内存分配器
        log.info("b.ByteBufAllocator: " + b.alloc());
    }

    //处理器类:演示使用Context来获取ByteBuf
    static class AllocDemoHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {
            printByteBuf("入站的ByteBuf", (ByteBuf) msg);
            log.debug(new String(((ByteBuf) msg).array(),((ByteBuf) msg).readerIndex(),((ByteBuf) msg).writerIndex(), CharsetUtil.UTF_8));
            //ctx.channel().config().setAllocator(PooledByteBufAllocator.DEFAULT);
            ByteBuf buf = ctx.alloc().buffer();
            System.out.println("buf:"+buf);
            //public ByteBufAllocator alloc() {
            //      return channel().config().getAllocator();
            //}
            //ctx.alloc()方法所获取的分配器是通道的缓冲区分配器。
            //该分配器可以通过Bootstrap引导类为通道进行配置,
            //也可以直接通过channel.config().setAllocator()
            //为通道设置一个缓冲区分配器。

            buf.writeInt(100);
//向模拟通道写入一个出站包,模拟数据出站,需要刷新通道才能获取到输出
            ctx.channel().writeAndFlush(buf);
        }
    }

    //测试用例入口
    public void testByteBufAlloc()
    {
        ChannelInitializer<EmbeddedChannel> i = new ChannelInitializer<>()
                {
                    protected void
                    initChannel(EmbeddedChannel ch)
                    {
                        ch.pipeline().addLast(new AllocDemoHandler());
                    }
                };
        EmbeddedChannel channel = new EmbeddedChannel(i);
//配置通道的缓冲区分配器,这里设置一个池化的分配器
        channel.config().setAllocator(PooledByteBufAllocator.DEFAULT);
        ByteBuf buf = channel.config().getAllocator().heapBuffer();
                //Unpooled.buffer();
        buf.writeInt(1);
//向模拟通道写入一个入站包,模拟数据入站
        channel.writeInbound(buf);
//获取通道的出站包
        ByteBuf outBuf = channel.readOutbound();
        System.out.println("outbuf:"+outBuf);

        printByteBuf("出站的ByteBuf", outBuf);
        int length = outBuf.readableBytes();
        byte[] array = new byte[length];
        outBuf.getBytes(outBuf.readerIndex(),array);
        log.info(new String(array,CharsetUtil.UTF_8));

    }

    public static void main(String[] args) {
        new AllocatorTest().testByteBufAlloc();
    }
}
