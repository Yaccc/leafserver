package org.yaccc.leafserver.common;

/**
 * Created by xiezhaodong  on 2018/6/9
 */
public class LeafServerConstants {

    public enum SegmentNumStatus {
        START, END
    }


    public static final long DATABASE_ERROR = -1L;
    public static final long NOT_FOUND_ID_WAIT_LOAD = -2L;
    public static final long STEP_IS_TOO_SHORT = 0L;
}
