package NettyGettingStarted;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class EventLoopServer {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        //创建一个独立的EventLoopGroup
        EventLoopGroup group = new DefaultEventLoopGroup();

        ServerBootstrap boot = new ServerBootstrap();

        boot.group(new NioEventLoopGroup(1),new NioEventLoopGroup(2))//boss只会找到一个线程进行绑定
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel nssc) throws Exception {
                    nssc.pipeline().addLast("handler1",new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug(buf);
                            log.debug(buf.toString(Charset.defaultCharset()));
                            super.channelRead(ctx,msg);
                            //ctx.fireChannelRead(msg);//将消息传达给下一个handler
                        }
                    });
//                    nssc.pipeline().addLast(group,"handler2",new ChannelInboundHandlerAdapter(){
//                        @Override
//                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                            ByteBuf buf = (ByteBuf) msg;
//                            log.debug(buf.toString(Charset.defaultCharset()));
//                        }
//                    });
                }
        });

        ChannelFuture channel =  boot.bind(9999).sync();

        log.debug("SUCCESS"+channel.channel().localAddress());

        channel.channel().closeFuture().sync();

    }
}


class EventClient{
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        client();
    }

    static void client() throws InterruptedException {
        Channel channel = new Bootstrap()
                //添加EventLoop
                .group(new NioEventLoopGroup())
                //选择客户端SocketChannel实现
                .channel(NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //添加处理器
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //socketChannel.pipeline().addLast(new StringEncoder(){});
//                        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
//                            @Override
//                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                super.channelRead(ctx, msg);
//                            }
//                        });
                        socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
                                ByteBuf buf =(ByteBuf) msg;
                                log.debug(new String(buf.array(),buf.readerIndex(),buf.writerIndex(),CharsetUtil.UTF_8));
                                super.channelRead(ctx,msg);
                            }
                        });
                    }
                }).connect(new InetSocketAddress("127.0.0.1",9999))
                .sync()//阻塞方法，直到连接建立
                .channel();

        log.debug(channel);

        ByteBuf buf = channel.alloc().buffer();
        buf.writeBytes("123".getBytes(CharsetUtil.UTF_8));
        channel.writeAndFlush(buf);

        channel.closeFuture().sync();

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