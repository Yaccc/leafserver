package org.yaccc.leafserver.common;

import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xiezhaodong  on 2018/2/13
 */

@Data
public class BizSegmentsBuffer {
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private volatile boolean isNextReady = false;
    private Segment[] segments = new Segment[2];
    public static final int FIRST_SEGMENT = 0, SECOND_SEGMENT = 1;
    private volatile int currentSegment = 0;

    public void setFirstSegment(Segment segment) {
        segments[0] = segment;
    }

    @Deprecated
    public void setSecondSegment(Segment segment) {
        segments[1] = segment;
    }

    /**
     * change to other segment,if current segment Consumed 90%
     */
    public void changeSegment() {
        isNextReady = false;
        currentSegment = currentSegment == FIRST_SEGMENT ? SECOND_SEGMENT : FIRST_SEGMENT;
    }

    public Segment getOtherSegment() {
        return segments[currentSegment == FIRST_SEGMENT ? SECOND_SEGMENT : FIRST_SEGMENT];
    }

    public void setOtherSegment(Segment segment) {
        if (currentSegment == 0) {
            segments[1] = segment;
            return;
        }
        segments[0] = segment;
    }

    /**
     * @return current segment
     */
    public Segment getCurrentSegment() {
        return segments[currentSegment];
    }


}
