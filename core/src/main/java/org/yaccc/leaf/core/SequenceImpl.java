package org.yaccc.leaf.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by xiezhaodong  on 2018/2/12
 */
@Slf4j
public class SequenceImpl implements Sequence {


    @Override
    public Long nextValue(@NonNull String appName,@NonNull String bizId) {




        return null;
    }

    @Override
    public List<Long> nextListValues(@NonNull String appName, @NonNull String bizId) {
        return null;
    }
}
