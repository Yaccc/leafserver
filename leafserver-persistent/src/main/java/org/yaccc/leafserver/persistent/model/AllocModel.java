package org.yaccc.leafserver.persistent.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by xiezhaodong  on 2018/2/23
 */
@Builder
@Data
@Deprecated
public class AllocModel {
    private Long min;
    private Long max;
    private Integer step;
}
