package org.yaccc.leafserver.persistent.IService;

import lombok.NonNull;
import org.yaccc.leafserver.common.Segment;

/**
 * Created by xiezhaodong  on 2018/6/10
 */
public interface ISequenceService {


    Segment buildSegment(@NonNull String appName, @NonNull String key);

    void changeStep(@NonNull String appName, @NonNull String key, int step);
}
