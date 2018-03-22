package org.yaccc.leafserver.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.yaccc.leafserver.biz.BizInstance;
import org.yaccc.leafserver.common.BizSegmentsBuffer;
import org.yaccc.leafserver.common.LeafServerUtils;
import org.yaccc.leafserver.common.Result;
import org.yaccc.leafserver.common.Segment;
import org.yaccc.leafserver.persistent.service.SequenceService;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Created by xiezhaodong  on 2018/2/12
 */
@Slf4j
@Service
@ConfigurationProperties(prefix = "org.yaccc.leafserver.pool")
public class SequenceCoreImpl extends AsyncSegmentPollDefined implements SequenceCore {

    @Autowired
    SequenceService sequenceService;


    @Override
    public Result nextValue(@NonNull String appName, @NonNull String key) {
        Result.ResultBuilder rr = Result.builder();
        Set<BizInstance> allBizInstance = BizInstance.allBizInstance;
        BizInstance biz = new BizInstance();
        biz.setAppName(appName);
        biz.setKey(key);
        //if not contains returen -1;
        Iterator<BizInstance> iterator = allBizInstance.iterator();
        while (iterator.hasNext()) {//hang
            BizInstance instance = iterator.next();
            if (instance.equals(biz)) {
                rr.id(getIdFromInstance(instance));//must instance
            }
        }
        return LeafServerUtils.wrapperResult(rr.build());
    }

    private Long getIdFromInstance(BizInstance instance) {
        //lock instance ,instead of lock method
        synchronized (instance) {
            BizSegmentsBuffer bizSegmentsBuffer = instance.getBizSegmentsBuffer();
            if (bizSegmentsBuffer == null) {
                //first segment init
                Segment segment = sequenceService.buildSegment(instance.getAppName(), instance.getKey());
                if (segment != null) {
                    bizSegmentsBuffer = new BizSegmentsBuffer();
                    bizSegmentsBuffer.setFirstSegment(segment);
                    bizSegmentsBuffer.setCurrentSegment(BizSegmentsBuffer.FIRST_SEGMENT);
                    instance.setBizSegmentsBuffer(bizSegmentsBuffer);
                    return segment.getLongFactory().getAndIncrement();
                }
            } else {
                Segment currentSegment = bizSegmentsBuffer.getCurrentSegment();
                //current segment can get next id;
                if (!bizSegmentsBuffer.isNextReady()
                        && currentSegment.needUpdateOtherSegment(90)
                        && bizSegmentsBuffer.getIsRunning().compareAndSet(false, true)) {
                    asyncUpdateSegment(instance, bizSegmentsBuffer);
                }

                Long segmentId = LeafServerUtils.getSegmentId(currentSegment);
                if (segmentId != null) {
                    log.debug("get result is {},app is [{}] ,key is [{}]", segmentId, instance.getAppName(), instance.getKey());
                    return segmentId;
                } else {
                    //current segment have no id;
                    //change segment
                    Segment otherSegment = bizSegmentsBuffer.getOtherSegment();
                    if (otherSegment != null && otherSegment.isInitCompleted()) {
                        bizSegmentsBuffer.changeSegment();
                        Segment nowSegment = bizSegmentsBuffer.getCurrentSegment();
                        return LeafServerUtils.getSegmentId(nowSegment);//maybe is not null,nedd fix it
                    }
                    return -2L;//all have no id
                }

            }
        }

        return -1L;//DB returen null
    }

    @PostConstruct
    public void init() {
        log.info("-------------init post construct method------------------------ ");
        createAndGetPool();
    }

    private void asyncUpdateSegment(BizInstance instance, BizSegmentsBuffer bizSegmentsBuffer) {
        ExecutorService andGetPool = createAndGetPool();
        andGetPool.execute(() -> {
            try {
                Segment segment = sequenceService.buildSegment(instance.getAppName(), instance.getKey());
                if (segment != null) {
                    bizSegmentsBuffer.setOtherSegment(segment);
                    bizSegmentsBuffer.setNextReady(true);//volatile write
                }
            } finally {
                bizSegmentsBuffer.getIsRunning().set(false);
            }

        });
    }

    @Override
    public List<Result> nextListValues(@NonNull String appName, @NonNull String key) {
        return null;
    }

}
