package org.yaccc.leaf.biz;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xiezhaodong  on 2018/2/12
 */
@Builder(builderMethodName = "creater",buildMethodName = "create")
@NoArgsConstructor
@Data
public class BizInstance {

    private String appName;

    private String bizId;





}
