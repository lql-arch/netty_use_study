package NettyGettingStarted;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutionException;

public class promise {
    private static final Logger log = LogManager.getLogger();
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop eventLoop = new NioEventLoopGroup().next();

        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

        new Thread(()->{
            log.info("计算");
            try {
                Thread.sleep(1000);
                promise.setSuccess(80);
            } catch (InterruptedException e) {
                promise.setFailure(e);
            }

        }).start();

        log.info("...");

        log.debug(promise.get());

    }
}
