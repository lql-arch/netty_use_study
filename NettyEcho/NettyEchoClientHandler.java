package NettyEcho;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyEchoClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LogManager.getLogger();
    public static final NettyEchoClientHandler INSTANCE = new NettyEchoClientHandler();
    //入站处理方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int len = byteBuf.readableBytes();
        byte[] arr = new byte[len];
        byteBuf.getBytes(0, arr);
        //log.info("client received: " + new String(arr, StandardCharsets.UTF_8));

        //释放ByteBuf的两种方法
        //方法一:手动释放ByteBuf
        //byteBuf.release();
        //方法二:调用父类的入站方法,将msg向后传递
        super.channelRead(ctx,msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        super.channelReadComplete(ctx);
    }
}