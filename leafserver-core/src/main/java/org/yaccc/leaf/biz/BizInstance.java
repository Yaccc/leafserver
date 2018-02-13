package org.yaccc.leaf.biz;

import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yaccc.leaf.model.BizSegmentsBuffer;
import org.yaccc.leaf.persistent.SequenceDao;
import org.yaccc.leaf.persistent.model.CoreTable;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * Created by xiezhaodong  on 2018/2/12
 */
@Setter
@Getter
@EqualsAndHashCode(of = {"appName","key"})
@Repository
@Slf4j
@NoArgsConstructor
public class BizInstance {


    @Autowired
    private SequenceDao sequenceDao;

    /**
     * appName, Which service is your service
     */
    private String appName;
    /**
     * service's function which  is it use
     */
    private String key;
    /**
     * step
     */
    private Integer step;
    /**
     * num segments,obtain two segment.lazy init
     */
    private BizSegmentsBuffer bizSegmentsBuffer;//
    /**
     * must be concurrentHashSet .keep consistency
     */
    public final static Set<BizInstance> allBizInstance= Sets.newConcurrentHashSet();
    @PostConstruct
    private void init(){
        log.info("--------------------------start init leafserver info------------------------------");
        List<CoreTable> allBizInfo = sequenceDao.getAllBizInfo();
        allBizInfo.parallelStream().forEach(coreTable -> {
            BizInstance bizInstance = new BizInstance();
            bizInstance.setAppName(coreTable.getAppName());
            bizInstance.setKey(coreTable.getKey());
            bizInstance.setStep(bizInstance.getStep());
            allBizInstance.add(bizInstance);
            log.info("|init appname:[{}] key:[{}] step:[{}] success!",coreTable.getAppName(),coreTable.getKey(),coreTable.getStep());

        });
        log.info("-------------------------init leafserver all biz info SUCCESS!!!!-------------------------");

    }


}
