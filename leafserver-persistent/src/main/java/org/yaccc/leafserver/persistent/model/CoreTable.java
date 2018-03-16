package org.yaccc.leafserver.persistent.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by xiezhaodong  on 2018/2/13
 */
@Data
public class CoreTable {

    private Long id;
    private String appName;
    private String key;
    private Integer step;
    private Long nowMaxId;
    private String descText;
    private Date createTime;
    private Date modifyTime;

}
