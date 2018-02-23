package org.yaccc.leaf.persistent.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by xiezhaodong  on 2018/2/13
 */
@Builder
@Data
public class Segment {
    private Long min;
    private Long max;
    private Integer step;
}
