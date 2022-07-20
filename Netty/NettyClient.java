package Netty;

import decoderAndEncoder.Byte2IntegerDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NettyClient {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new Byte2IntegerDecoder());
                        sc.pipeline().addLast(new NettyClientHandle());
                    }
                });
        ChannelFuture future = bootstrap.connect("127.0.0.1",9988).sync();
        future.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}

