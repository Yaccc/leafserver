package org.yaccc.leafserver.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by xiezhaodong  on 2018/2/13
 */

@Getter
@Setter
@Builder
public class BizSegmentsBuffer {
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Segment[] segments = new Segment[2];
    public static final int FIRST_SEGMENT = 0, SECOND_SEGMENT = 1;
    private volatile int currentSegment = 0;

    public void setFirstSegment(Segment segment) {
        segments[0] = segment;
    }

    public void setSecondSegment(Segment segment) {
        segments[1] = segment;
    }

    /**
     * change to other segment,if current segment Consumed 90%
     */
    public void changeSegment() {
        currentSegment = currentSegment == FIRST_SEGMENT ? SECOND_SEGMENT : FIRST_SEGMENT;
    }

    public Segment getOtherSegment() {
        return segments[currentSegment == FIRST_SEGMENT ? SECOND_SEGMENT : FIRST_SEGMENT];
    }

    public void setOtherSegment(Segment segment) {
        if (currentSegment == 0) {
            segments[1] = segment;
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
