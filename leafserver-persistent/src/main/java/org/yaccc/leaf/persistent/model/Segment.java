package org.yaccc.leaf.persistent.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiezhaodong  on 2018/2/13
 */
@Builder
public class Segment {
    @Getter
    @Setter
    private Long min;
    @Getter
    @Setter
    private Long max;
    @Getter
    @Setter
    private Integer step;

    @Getter
    @Setter
    private volatile boolean initCompleted = false;

    private AtomicLong current = null;

    public AtomicLong getCurrent() {
        if (initCompleted)
            return new AtomicLong(min);
        return null;
    }
}
