package NettyGettingStarted;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

public class NettyGettingStarted {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {

        //创建启动器
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.
                //BossEventLoop，WorkerEventLoop（selector，thread）。
                group(new NioEventLoopGroup()) //监听 accept事件
                //选择服务器的ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //boss处理连接，worker（child）处理读写，决定worker（child）能执行那些操作（handler）
                .childHandler(
                        //channel 代表与客户进行数据读写的通道 Initializer 初始化 ，添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringDecoder()); //将ByteBuf转化为字符串
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){//自定义
                            @Override//读事件
                            public void channelRead(ChannelHandlerContext ctx,Object msg)throws Exception{
                                //打印上一步转换好的字符串
                                log.debug(msg);

                            }
                        });
                    }
                }).bind(9999);
    }
}
class helloClient{
    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                //添加EventLoop
                .group(new NioEventLoopGroup())
                //选择客户端SocketChannel实现
                .channel(NioSocketChannel.class)
                //添加处理器
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringEncoder());

                    }
                }).connect(new InetSocketAddress("127.0.0.1",9999))
                .sync()//阻塞方法，直到连接建立
                .channel()
                .writeAndFlush("hello,world.");


    }
}