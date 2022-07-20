package NettyGettingStarted;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


public class future {
    private static final Logger log = LogManager.getLogger();
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventExecutors = group.next();

        Future<Integer> future = eventExecutors.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("计算");
                Thread.sleep(1000);
                return 70;
            }
        });
//        log.info(future.get());
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                log.info(future.getNow());
            }
        });
    }
}
