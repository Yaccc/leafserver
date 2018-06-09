package org.yaccc.leafserver.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.yaccc.leafserver.biz.BizInstance;
import org.yaccc.leafserver.common.*;
import org.yaccc.leafserver.persistent.IService.ISequenceService;

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
public class SequenceCoreImpl extends AsyncSegmentPollDefined implements ISequenceCore {

    @Autowired
    private ISequenceService sequenceService;

    @Autowired
    private AutoAdaptQPS autoAdaptQPS;



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
                return LeafServerUtils.wrapperResult(rr.build());
            }
        }
        return Result.builder()
                .errMsg("have no biz ,please regist your biz to leafserver")
                .isSuccess(false)
                .build();


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
                    long segmentId = segment.getLongFactory().getAndIncrement();
                    return segmentId;
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
                    return LeafServerConstants.NOT_FOUND_ID_WAIT_LOAD;//all have no id
                }

            }
        }

        return LeafServerConstants.DATABASE_ERROR;//DB returen null
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


    private void sceduleChangeStepThread(long id, Segment segment, BizInstance instance) {
        LeafServerConstants.SegmentNumStatus status;
        if (id == segment.getMin()) {
            status = LeafServerConstants.SegmentNumStatus.START;
        } else if (id == segment.getMax()) {
            status = LeafServerConstants.SegmentNumStatus.END;
        } else {
            //不是开头和末尾,直接跳过不记录时间
            return;
        }
        //时间非常重要,不能在调度线程里面生成,影响精度
        //由于调度的原因,可能有些小问题,除非完全同步运行,异步方式可能end在start之前进入
        long now = System.currentTimeMillis();
        asyncGetSegment.execute(() -> autoAdaptQPS.recordSegmentStartAndEndTime(status, segment, instance, now));
    }

}
