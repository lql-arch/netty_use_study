package NettyEcho;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.FutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

public class NettyEchoClient {
    private static final Logger log = LogManager.getLogger();
    private int serverPort;
    private String serverIp;
    Bootstrap b = new Bootstrap();

    public NettyEchoClient(String ip, int port) {
        this.serverPort = port;
        this.serverIp = ip;
    }

    public void runClient() {
//创建反应器轮询组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        try {
//1 设置反应器轮询组
            b.group(workerLoopGroup);
//2 设置nio类型的通道
            b.channel(NioSocketChannel.class);
//3 设置监听端口
            //b.remoteAddress(serverIp, serverPort);
//4 设置通道的参数
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
//5 装配子通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个通道
                protected void initChannel(SocketChannel ch) {
//管理子通道中的Handler
//向子通道流水线添加一个Handler
                    ch.pipeline().addLast(new NettyEchoClientHandler());
                }
            });

            ChannelFuture f = b.connect(new InetSocketAddress(serverIp, serverPort));
            f.addListener((FutureListener) -> {
                if (FutureListener.isSuccess()) {
                    log.info("EchoClient客户端连接成功!");
                } else {
                    log.info("EchoClient客户端连接失败!");
                }
            }).sync();
//阻塞,直到连接成功
            //f.sync();

            Channel channel = f.channel();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                log.info("请输入发送内容:");
//获取输入的内容
                String next = scanner.next();
                if(Objects.equals(next, "exit")){
                    break;
                }
                byte[] bytes = next.getBytes(StandardCharsets.UTF_8);
//发送ByteBuf
                ByteBuf buffer = channel.alloc().buffer();
                buffer.writeBytes(bytes);
                channel.writeAndFlush(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//优雅关闭EventLoopGroup,
//释放掉所有资源,包括创建的线程
            workerLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyEchoClient("bronya",9999).runClient();
    }
}

class test{
    public static void main(String[] args) {
        new NettyEchoClient("bronya",9999).runClient();
    }
}
