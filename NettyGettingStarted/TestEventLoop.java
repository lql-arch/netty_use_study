package NettyGettingStarted;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class TestEventLoop {
    private static final Logger log = LogManager.getLogger();

    public static void main(String[] args) {
        EventLoopGroup nGroup = new NioEventLoopGroup(2); // io 事件，普通任务，定时任务
        EventLoopGroup dGroup = new DefaultEventLoop(); // 普通任务，定时任务

        //System.out.println(NettyRuntime.availableProcessors());
        log.debug(nGroup.next());
        log.debug(nGroup.next());
        log.debug(nGroup.next());
        log.debug(nGroup.next());

        //普通任务
        //submit
//        nGroup.next().execute(()->{
//            log.debug("ok");
//        });

        //定时任务
//        nGroup.next().scheduleAtFixedRate(()->{
//            log.debug("ok");
//        },1,1, TimeUnit.SECONDS);



    }

}
