package org.yaccc.leaf.core;

import java.util.List;

/**
 * Created by xiezhaodong  on 2018/2/12
 */

public interface Sequence {

    Long nextValue(String appName,String key);


    List<Long> nextListValues(String appName,String key);

}
