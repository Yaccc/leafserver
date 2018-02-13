package org.yaccc.leaf.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.yaccc.leaf.biz.BizInstance;

import java.util.List;
import java.util.Set;

/**
 * Created by xiezhaodong  on 2018/2/12
 */
@Slf4j
public class SequenceImpl implements Sequence {


    @Override
//    @Synchronized
    public Long nextValue(@NonNull String appName,@NonNull String key) {
        Set<BizInstance> allBizInstance = BizInstance.allBizInstance;
        BizInstance bizInstance=new BizInstance();
        bizInstance.setAppName(appName);
        bizInstance.setKey(key);
        if (allBizInstance.contains(bizInstance)){



        }

        return -1L;
    }

    @Override
    public List<Long> nextListValues(@NonNull String appName, @NonNull String key) {
        return null;
    }
}
