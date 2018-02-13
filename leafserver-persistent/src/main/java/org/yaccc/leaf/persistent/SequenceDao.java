package org.yaccc.leaf.persistent;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.yaccc.leaf.persistent.model.CoreTable;

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
     * @return ALL CoreTable
     */
    @Select("select * from core_table ")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "app_name",property = "appName"),
            @Result(column = "key",property = "key"),
            @Result(column = "now_max_id",property = "nowMaxId"),
            @Result(column = "step",property = "step")
    })
    List<CoreTable> getAllBizInfo();

}
