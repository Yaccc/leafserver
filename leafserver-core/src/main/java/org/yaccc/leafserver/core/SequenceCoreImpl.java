package org.yaccc.leafserver.core;

import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaccc.leafserver.biz.BizInstance;
import org.yaccc.leafserver.common.LeafServerUtils;
import org.yaccc.leafserver.common.Result;
import org.yaccc.leafserver.model.BizSegmentsBuffer;
import org.yaccc.leafserver.persistent.model.Segment;
import org.yaccc.leafserver.persistent.service.SequenceService;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by xiezhaodong  on 2018/2/12
 */
@Slf4j
public class SequenceCoreImpl implements Sequence {
    @Autowired
    SequenceService sequenceService;

    @Override
//    @Synchronized
    public Result nextValue(@NonNull String appName, @NonNull String key) {
        Result.ResultBuilder rr = Result.builder();
        Set<BizInstance> allBizInstance = BizInstance.allBizInstance;
        BizInstance biz = new BizInstance();
        biz.setAppName(appName);
        biz.setKey(key);
        //if not contains returen -1;
        Iterator<BizInstance> iterator = allBizInstance.iterator();
        while (iterator.hasNext()) {
            BizInstance instance = iterator.next();
            if (instance.equals(biz)) {
                rr.id(getIdFromInstance(instance));
            }
        }
        return LeafServerUtils.wrapperResult(rr.build());
    }

    @Synchronized
    private Long getIdFromInstance(BizInstance instance) {
        BizSegmentsBuffer bizSegmentsBuffer = instance.getBizSegmentsBuffer();
        if (bizSegmentsBuffer == null) {
            //first init
            Segment segment = sequenceService.buildSegment(instance.getAppName(), instance.getKey());
            segment.setInitCompleted(true);
            //
            bizSegmentsBuffer = BizSegmentsBuffer.builder().build();
            bizSegmentsBuffer.setFirstSegment(segment);
            return segment.getCurrent().incrementAndGet();
        } else {


        }

        return -1L;
    }

    @Override
    public List<Result> nextListValues(@NonNull String appName, @NonNull String key) {
        return null;
    }
}
