package org.yaccc.leaf.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.yaccc.leaf.persistent.model.Segment;

/**
 * Created by xiezhaodong  on 2018/2/13
 */

@Getter
@Setter
@Builder
public class BizSegmentsBuffer {

    private Segment[] segments;
    private int currentSegment;


}
