package org.yaccc.leafserver.core;

import lombok.Data;

import java.util.concurrent.*;

/**
 * Created by xiezhaodong  on 2018/3/18
 */
@Data
public abstract class AsyncSegmentPollDefined {

    protected boolean isUseFixedPool;
    protected int fixedPollSize;

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
        if (isUseFixedPool) {
            asyncGetSegment = Executors.newFixedThreadPool(fixedPollSize, threadFactory);
        }

        asyncGetSegment = new ThreadPoolExecutor(coreSize, maxCoreSize, keepAlive, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
        return asyncGetSegment;
    }

}
