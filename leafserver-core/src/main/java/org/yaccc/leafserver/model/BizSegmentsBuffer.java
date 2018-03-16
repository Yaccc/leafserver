package org.yaccc.leafserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.yaccc.leafserver.persistent.model.Segment;

/**
 * Created by xiezhaodong  on 2018/2/13
 */

@Getter
@Setter
@Builder
public class BizSegmentsBuffer {

    private Segment[] segments = new Segment[2];
    private int currentSegment = 0;

    public void setFirstSegment(Segment segment) {
        segments[0] = segment;
    }

    public void setSecondSegment(Segment segment) {
        segments[1] = segment;
    }


}
