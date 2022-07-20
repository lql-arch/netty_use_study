package NettyGettingStarted;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;

public class EventLoopServer {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        //创建一个独立的EventLoopGroup
        EventLoopGroup group = new DefaultEventLoopGroup();
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap boot = new ServerBootstrap();

        try {
            boot.group(boss,worker)//boss只会找到一个线程进行绑定
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nssc) throws Exception {
                            //nssc.pipeline().addLast(new LoggingHandler());
                            //nssc.pipeline().addLast(new StringDecoder());
                            nssc.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.debug("1");
//                                    ByteBuf buf = (ByteBuf) msg;
//                                    log.debug(buf);
//                                    log.debug(buf.toString(Charset.defaultCharset()));
                                    super.channelRead(ctx,msg);
                                }
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    super.exceptionCaught(ctx, cause);
                                }
                            });
                            nssc.pipeline().addLast("h2",new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.debug("2");
                                    ctx.channel().writeAndFlush(ByteBufAllocator.DEFAULT.heapBuffer().writeBytes("sad".getBytes(CharsetUtil.UTF_8)));
                                    super.channelRead(ctx, msg);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    super.exceptionCaught(ctx, cause);
                                }
                            } );
                            nssc.pipeline().addLast("h3",new ChannelOutboundHandlerAdapter(){
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    log.debug("4");
                                    super.write(ctx, msg, promise);
                                }
                            } );
                            nssc.pipeline().addLast("h4",new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.debug("3");
                                    ByteBuf buf = (ByteBuf) msg;
                                    log.debug(buf.toString(Charset.defaultCharset()));
                                    super.channelRead(ctx, msg);
                                }
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    super.exceptionCaught(ctx, cause);
                                }
                            } );
                        }
                    });

            ChannelFuture channel = boot.bind(9999).sync();

            log.debug("SUCCESS" + channel.channel().localAddress());

            //channel.channel().close();
            channel.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
