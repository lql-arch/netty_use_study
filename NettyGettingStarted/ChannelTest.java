package NettyGettingStarted;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ChannelTest {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try{
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    //.localAddress(9999)
                    .localAddress(new InetSocketAddress(9998))
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列中等待
                    //.childOption(ChannelOption.TCP_NODELAY,true)  关闭Nagle 算法
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ByteBuf buf = Unpooled.copiedBuffer("#".getBytes(StandardCharsets.UTF_8));
                            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(2,buf){
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf byteBuf = (ByteBuf) msg;
                                    log.debug(byteBuf.toString(CharsetUtil.UTF_8));
                                }
                            });
//                            sc.pipeline().addLast(new LineBasedFrameDecoder(2048){
//                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                    ByteBuf buf = (ByteBuf) msg;
//                                    log.debug(buf.toString(CharsetUtil.UTF_8));
//                                }
//                            });
//                            sc.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//                                @Override
//                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                    ByteBuf buf = (ByteBuf) msg;
//                                    log.debug(buf.toString(CharsetUtil.UTF_8));
//                                }
//                            });
//                            sc.pipeline().addLast(new StringDecoder());
//                            sc.pipeline().addLast(new ChannelInboundHandlerAdapter(){
//                                @Override
//                                public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
//                                    log.debug(msg);
//                                }
//                            });
                        }
                    });


            ChannelFuture future = bootstrap.bind().sync();

            EmbeddedChannel w = new EmbeddedChannel();
//            log.debug(w.writeInbound("123"));
//            log.debug((Message) w.readOutbound());

            log.debug(" 服务器启动成功，监听端口: " + future.channel().localAddress());

            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}


class ClientChannelTest{
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        ChannelFuture channelFuture = bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                }).connect("127.0.0.1",9998);

                channelFuture.sync();

                Channel channel = channelFuture.channel();
                log.debug("{}",channel);

                channel.writeAndFlush("12313#");
//                //log.debug("");
//                //Thread.sleep(1000);
                channel.writeAndFlush("12313");

//                channel.writeAndFlush(Unpooled.copiedBuffer("\n12345\n",CharsetUtil.UTF_8));
//                channel.writeAndFlush(Unpooled.copiedBuffer("12345",CharsetUtil.UTF_8));

//        channel.writeAndFlush(Unpooled.copiedBuffer("#123#123",CharsetUtil.UTF_8));
    }
}