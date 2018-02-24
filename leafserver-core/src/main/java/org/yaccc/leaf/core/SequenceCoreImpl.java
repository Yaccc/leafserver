package org.yaccc.leaf.core;

import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaccc.leaf.biz.BizInstance;
import org.yaccc.leaf.model.BizSegmentsBuffer;
import org.yaccc.leaf.persistent.model.Segment;
import org.yaccc.leaf.persistent.service.SequenceService;

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
    public Long nextValue(@NonNull String appName,@NonNull String key) {
        Set<BizInstance> allBizInstance = BizInstance.allBizInstance;
        BizInstance biz=new BizInstance();
        biz.setAppName(appName);
        biz.setKey(key);
        //if not contains returen -1;
        Iterator<BizInstance> iterator = allBizInstance.iterator();
        while (iterator.hasNext()){
            BizInstance instance = iterator.next();
            if (instance.equals(biz)){
                return  getIdFromInstance(instance);
            }
        }
        return -1L;
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
    public List<Long> nextListValues(@NonNull String appName, @NonNull String key) {
        return null;
    }
}
