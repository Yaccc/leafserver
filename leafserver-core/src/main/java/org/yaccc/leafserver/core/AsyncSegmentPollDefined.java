package org.yaccc.leafserver.core;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by xiezhaodong  on 2018/3/18
 */
@Data
@Slf4j
public abstract class AsyncSegmentPollDefined {

    protected boolean useFixedPool;
    protected int fixedPoolSize;

    protected int coreSize;
    protected int maxCoreSize;
    protected int keepAlive;

    protected ExecutorService asyncGetSegment = null;

    public ExecutorService createAndGetPool() {
        if (asyncGetSegment != null) return asyncGetSegment;
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("async-update-segment");
            return t;
        };
        if (useFixedPool) {
            Preconditions.checkArgument(fixedPoolSize != 0, "fiexdPoolSize can not equals " + fixedPoolSize);
            asyncGetSegment = Executors.newFixedThreadPool(fixedPoolSize, threadFactory);
            log.info("async pool use fixedThreadPool,pollsize is {}", fixedPoolSize);
            return asyncGetSegment;
        }
        Preconditions.checkArgument(coreSize != 0, "coreSize can not equals " + coreSize);
        Preconditions.checkArgument(maxCoreSize != 0, "maxCoreSize can not equals " + maxCoreSize);
        Preconditions.checkArgument(keepAlive != 0, "keepAlive can not equals " + keepAlive);
        asyncGetSegment = new ThreadPoolExecutor(coreSize, maxCoreSize, keepAlive, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
        log.info("Use threadpool coreSize is {},maxCoreSize is {},keepAlive is {}", coreSize, maxCoreSize, keepAlive);
        return asyncGetSegment;
    }

}
