package NettyGettingStarted;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class EventClient {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        client();
    }

    static void client() throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ChannelFuture channelFuture = new Bootstrap()
                    //添加EventLoop
                    .group(worker)
                    //选择客户端SocketChannel实现
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //添加处理器
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                            //socketChannel.pipeline().addLast(new LoggingHandler());
                            //socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.debug("1");
                                    super.channelRead(ctx, msg);
                                }
                            });
                            socketChannel.pipeline().addLast("h2",new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.debug("2");
                                    super.channelRead(ctx, msg);
                                }
                            } );
                            socketChannel.pipeline().addLast("h3",new ChannelOutboundHandlerAdapter(){
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    log.debug("4");
                                    super.write(ctx, msg, promise);
                                }
                            } );
                            socketChannel.pipeline().addLast("h4",new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    log.debug("3");
                                    ByteBuf buf = (ByteBuf) msg;
                                    //log.debug(new String(buf.array(), buf.readerIndex(), buf.writerIndex(), CharsetUtil.UTF_8));
                                    log.debug(buf.toString(CharsetUtil.UTF_8));
                                    super.channelRead(ctx, msg);
                                }
                            } );
                        }
                    }).connect(new InetSocketAddress("127.0.0.1", 9999));

//1.sync();
            Channel channel = channelFuture.sync()//阻塞方法，直到连接建立
                    .channel();

            log.debug(channel);

            channelFuture.addListener((future -> {
                if (future.isSuccess()) {
                    log.debug("connect success");
                } else {
                    log.debug("connect failed.");
                }
            })).sync();

            //2.addListener()
//        channelFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                Channel channel = future.channel();
//                log.info("{}",channel);
//            }
//        }).sync();

            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    log.info("Please input:");
                    String s = scanner.nextLine();
                    if ("q".equals(s)) {
                        channel.close();
                        break;
                    }
                    ByteBuf buf = Unpooled.buffer();
                    buf.writeBytes(s.getBytes(CharsetUtil.UTF_8));
                    channel.writeAndFlush(buf);
                }
            }, "input").start();

            channel.closeFuture().sync();

            channelFuture.addListener((future -> {
                if (future.isDone()) {
                    log.debug("close success");
                } else {
                    log.debug("close failed.");
                }
            })).sync();
        } finally {
            worker.shutdownGracefully();
        }

    }
}

class EventClient1{
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        EventClient.client();

    }
}

class EventClient2{
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        EventClient.client();

    }
}

class EventClient3{
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        EventClient.client();

    }
}