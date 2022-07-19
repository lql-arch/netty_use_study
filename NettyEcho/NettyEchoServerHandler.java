package NettyEcho;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LogManager.getLogger();

    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf in = (ByteBuf) msg;
        //log.info("msg type: " + (in.hasArray()?"堆内存":"直接内存"));
        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        log.info("server received: " + new String(arr, StandardCharsets.UTF_8));
        //log.debug("写回前,msg.refCnt:" + ((ByteBuf) msg).refCnt());
//写回数据,异步任务
        ChannelFuture f = ctx.writeAndFlush(msg);
//        log.debug("写回后,msg.refCnt:" + ((ByteBuf) msg).refCnt());
//        f.addListener((ChannelFutureListener) -> {
//            log.debug("写回后,msg.refCnt:" + ((ByteBuf) msg).refCnt());
//        });
    }
}
