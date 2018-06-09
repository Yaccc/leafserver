package org.yaccc.leafserver.persistent;

import org.apache.ibatis.annotations.*;
import org.yaccc.leafserver.persistent.model.CoreTable;

import java.util.List;

/**
 * Created by xiezhaodong  on 2018/2/12
 * access mysql and more
 */
@Mapper
public interface SequenceDao {

    @Select("select 1")
    int test();

    /**
     * select all biz info form mysql
     *
     * @return ALL CoreTable
     */
    @Select("select * from core_table ")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "app_name", property = "appName"),
            @Result(column = "key", property = "key"),
            @Result(column = "now_max_id", property = "nowMaxId"),
            @Result(column = "step", property = "step")
    })
    List<CoreTable> getAllBizInfo();


    @Update("update core_table set now_max_id=now_max_id+step where app_name=#{appName} and `key`=#{key}")
    int updateMaxId(@Param("appName") String appName, @Param("key") String key);

    @Update("update core_table set step=#{step} where app_name=#{appName} and `key`=#{key}")
    int changeStep(@Param("appName") String appName, @Param("key") String key, @Param("step") int step);

    @Select("select * from core_table WHERE app_name=#{appName} and `key`=#{key}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "app_name", property = "appName"),
            @Result(column = "key", property = "key"),
            @Result(column = "now_max_id", property = "nowMaxId"),
            @Result(column = "step", property = "step")
    })
    CoreTable getOneBizInfo(@Param("appName") String appName, @Param("key") String key);


}
