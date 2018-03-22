package org.yaccc.leafserver.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiezhaodong  on 2018/2/13
 */
@Builder
//@Contended// why I will add it ,False Sharing?
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
    @Getter
    @Setter
    private AtomicLong longFactory = null;

    /**
     * eg:threshold==90 ,then,Remaining 90% ,retur true
     *
     * @param threshold
     * @return
     */
    public boolean needUpdateOtherSegment(int threshold) {

        if ((getMax() - longFactory.longValue()) <= (step * threshold / 100)) return true;

        return false;
    }


}
