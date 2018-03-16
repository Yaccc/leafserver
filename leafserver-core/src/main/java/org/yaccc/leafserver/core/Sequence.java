package org.yaccc.leafserver.core;

import org.yaccc.leafserver.common.Result;

import java.util.List;

/**
 * Created by xiezhaodong  on 2018/2/12
 */

public interface Sequence {

    Result nextValue(String appName, String key);


    List<Result> nextListValues(String appName, String key);

}
