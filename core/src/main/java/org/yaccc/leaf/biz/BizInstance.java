package org.yaccc.leaf.biz;

import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by xiezhaodong  on 2018/2/12
 */
@Builder
@Data
@EqualsAndHashCode(of = {"appName","bizId"})
@Repository
public class BizInstance {

    private String appName;

    private String bizId;

    public static Set<BizInstance> all= Sets.newHashSet();
    @PostConstruct
    private void init(){

        


    }


}
