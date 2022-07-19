package bytebuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class bufUtil {
    private static final Logger log = LogManager.getLogger();

    public void SetBufUtil() {
        ServerBootstrap b = new ServerBootstrap();
//设置通道的参数
        b.option(ChannelOption.SO_KEEPALIVE, true);
//设置父通道的缓冲区分配器
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
//设置子通道的缓冲区分配器
        b.childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);
    }

    public void showAlloc() {
        ByteBuf buffer = null;
//方法1:通过默认分配器分配
//初始容量为9、最大容量为100的缓冲区
        buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        log.debug(buffer+":"+buffer.capacity()+" "+buffer.maxCapacity());
//方法2:通过默认分配器分配
//初始容量为256、最大容量为Integer.MAX_VALUE的缓冲区
        buffer = ByteBufAllocator.DEFAULT.buffer();
        log.debug(buffer+":"+buffer.capacity()+" "+buffer.maxCapacity());
//方法3:非池化分配器,分配Java的堆(Heap)结构内存缓冲区
        buffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        log.debug(buffer+":"+buffer.capacity()+" "+buffer.maxCapacity());
//方法4:池化分配器,分配由操作系统管理的直接内存缓冲区
        buffer = PooledByteBufAllocator.DEFAULT.directBuffer();
        log.debug(buffer+":"+buffer.capacity()+" "+buffer.maxCapacity());
//其他方法
        buffer = PooledByteBufAllocator.DEFAULT.compositeBuffer();
        log.debug(buffer+":"+buffer.capacity()+" "+buffer.maxCapacity());
    }

    public static void main(String[] args) {
        new bufUtil().showAlloc();
    }
}