package org.yaccc.leafserver.common;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xiezhaodong  on 2018/3/16
 */
@NoArgsConstructor
@Data
@Builder
public class Result {
    private long id;
    private boolean isSuccess = false;
    private String errMsg;
}
