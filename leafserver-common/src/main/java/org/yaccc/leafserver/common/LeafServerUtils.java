package org.yaccc.leafserver.common;

/**
 * Created by xiezhaodong  on 2018/3/17
 */
public class LeafServerUtils {

    public static Result wrapperResult(Result result) {
        long id = result.getId();
        if (id < 0) {
            switch ((int) id) {
                case -1:
                    result.setErrMsg("xxxxx-1");
                    break;
                case -2:
                    result.setErrMsg("xxxx-2");
                    break;
                default:
                    break;
            }
        } else {
            result.setSuccess(true);
        }
        return result;
    }
}
