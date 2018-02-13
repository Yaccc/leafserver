package org.yaccc.leaf.persistent;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by xiezhaodong  on 2018/2/12
 * 访问数据库
 */
@Mapper
public interface SequenceDao {

    @Select("select 1")
    int test();

}
