package org.yaccc.leafserver.core;

import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaccc.leafserver.Iface.IExtendFace;
import org.yaccc.leafserver.biz.BizInstance;
import org.yaccc.leafserver.common.LeafServerConstants;
import org.yaccc.leafserver.common.Segment;
import org.yaccc.leafserver.persistent.IService.ISequenceService;

import java.util.Map;

/**
 * Created by xiezhaodong  on 2018/6/10
 */
@Component
@Slf4j
public class AutoAdaptQPS {
    private Map<Pair<String, String>, Pair<Long, Long>> recordSegmentCache = Maps.newConcurrentMap();

    @Autowired
    ISequenceService sequenceService;
    private static IExtendFace iExtendFace;

    static {
        iExtendFace = null;
    }

    public void recordSegmentStartAndEndTime(@NonNull LeafServerConstants.SegmentNumStatus status, @NonNull Segment segment, @NonNull BizInstance instance, long now) {

        Pair<String, String> app = Pair.of(instance.getAppName(), instance.getKey());
        Pair<Long, Long> times = recordSegmentCache.get(app);
        synchronized (instance) {
            if (times == null) {//init
                //first time must be start
                Pair<Long, Long> _time = Pair.of(now, 0L);
                recordSegmentCache.put(app, _time);
            } else {
                //sencond time must be end
                if (status == LeafServerConstants.SegmentNumStatus.END) {
                    //如果是END,如果是0代表是第二次
                    //不存在END END的情况
                    long cost = now - times.getKey();
                    //如果耗时小于db更新时间,说明要提高step了,但是提高多少呢?这个还是得计算qps what the fuck
                    //先默认扩大一倍吧,fuck
                    if (cost < iExtendFace.provideDataBaseQPS()) {
                        sequenceService.changeStep(instance.getAppName(), instance.getKey(), segment.getStep() << 1);
                    }
                } else if (status == LeafServerConstants.SegmentNumStatus.START) {
                    //在不为null的情况下,走到这一步还是Start,说明第二个Segment开始消费,记录开始时间
                    //清空结束时间
                    recordSegmentCache.put(app, Pair.of(now, 0L));
                }

            }
        }

    }

}
