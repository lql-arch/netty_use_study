package NettyGettingStarted;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PipelineHotOperateTester {
    private static final Logger log = LogManager.getLogger();

    static class SimpleInHandlerA extends
            ChannelInboundHandlerAdapter {
        public void channelRead(ChannelHandlerContext ctx, Object
                msg) throws Exception {
            log.info("入站处理器 A: 被回调 ");
            super.channelRead(ctx, msg);
            //从流水线删除当前业务处理器
            ctx.pipeline().remove(this);
        }
    }

    ;

    static class SimpleInHandlerB extends
            ChannelInboundHandlerAdapter {
        public void channelRead(ChannelHandlerContext ctx, Object
                msg) throws Exception {
            log.info("入站处理器 B: 被回调 ");
            super.channelRead(ctx, msg);
            //从流水线删除当前业务处理器
            ctx.pipeline().remove(this);
        }
    }


    static class SimpleInHandlerC extends
            ChannelInboundHandlerAdapter {
        public void channelRead(ChannelHandlerContext ctx, Object
                msg) throws Exception {
            log.info("入站处理器 C: 被回调 ");
            super.channelRead(ctx, msg);
            //从流水线删除当前业务处理器
            ctx.pipeline().remove(this);
        }
    }


    //省略SimpleInHandlerB、SimpleInHandlerC的定义
    //测试业务处理器的热拔插
    public void testPipelineHotOperating() throws InterruptedException {
        ChannelInitializer<EmbeddedChannel> i = new ChannelInitializer<EmbeddedChannel>() {
                    protected void initChannel(EmbeddedChannel ch) {
                        ch.pipeline().addLast(new SimpleInHandlerA());
                        ch.pipeline().addLast(new SimpleInHandlerB());
                        ch.pipeline().addLast(new SimpleInHandlerC());
                    }
                };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.setInt(1,23);
        int a = buf.getInt(1);
        log.debug(a);
        buf.writeInt(1);
        //第一次向通道写入站报文（或数据包）
        channel.writeInbound(buf);
        //Thread.sleep(1000);
        //第二次向通道写入站报文（或数据包）
        buf.writeInt(2);
        channel.writeInbound(buf);
        //Thread.sleep(1000);
        //第三次向通道写入站报文（或数据包）
        buf.writeInt(3);
        channel.writeInbound(buf);
    }

    public static void main(String[] args) throws InterruptedException {
        new PipelineHotOperateTester().testPipelineHotOperating();
    }
}
