package NettyGettingStarted;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InHandlerDemo extends ChannelInboundHandlerAdapter {
    private static final Logger log = LogManager.getLogger();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("被调用:handlerAdder()");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("被调用:handlerRemoved()");
        super.handlerRemoved(ctx);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("被调用:channelRegistered()");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("被调用:channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("被调用:channelActive()");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("被调用:channelInactive()");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("被调用:channelRead()");
        super.channelRead(ctx,msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("被调用:channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("被调用:userEventTriggered()");
        super.userEventTriggered(ctx,evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("被调用:channelWritabilityChanged()");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("被调用:exceptionCaught()");
        super.exceptionCaught(ctx,cause);
    }
}

class InHandlerDemoTests{
    public static void testInHandlerLifeCircle() throws InterruptedException {
        final InHandlerDemo inHandler = new InHandlerDemo();
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(inHandler);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);

        //模拟入站，向嵌入式通道写一个入站数据包
        channel.writeInbound(buf);
        channel.flush();

        //模拟入站，再写一个入站数据包
        channel.writeInbound(buf);
        channel.flush();

        //关闭通道
        channel.close();
    }

    public static void main(String[] args) throws InterruptedException {
        testInHandlerLifeCircle();
    }
}