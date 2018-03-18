package org.yaccc.leafserver.biz;

import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.yaccc.leafserver.common.BizSegmentsBuffer;
import org.yaccc.leafserver.persistent.SequenceDao;
import org.yaccc.leafserver.persistent.model.CoreTable;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiezhaodong  on 2018/2/12
 */
@Setter
@Getter
@EqualsAndHashCode(of = {"appName", "key"})
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
    public static volatile Set<BizInstance> allBizInstance = Sets.newConcurrentHashSet();

    @PostConstruct
    private void init() {
        log.info("--------------------------start init leafserver info------------------------------");
        List<CoreTable> allBizInfo = sequenceDao.getAllBizInfo();
        buildBizInstance(allBizInstance, allBizInfo);
        log.info("-------------------------init leafserver all biz info SUCCESS!!!!-------------------------");
        startTimer();
    }

    private void buildBizInstance(Set<BizInstance> allBizInstance, List<CoreTable> allBizInfo) {
        allBizInfo.parallelStream().forEach(coreTable -> {
            BizInstance bizInstance = new BizInstance();
            bizInstance.setAppName(coreTable.getAppName());
            bizInstance.setKey(coreTable.getKey());
            bizInstance.setStep(bizInstance.getStep());
            allBizInstance.add(bizInstance);
            log.info("|init appname:[{}], key:[{}], step:[{}] success!", coreTable.getAppName(), coreTable.getKey(), coreTable.getStep());

        });
    }


    /**
     * time to update allbizInstance
     */
    private void startTimer() {
        log.debug("timer start to update bizInstance");
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("biz-timer");
            return t;
        })
                .scheduleWithFixedDelay((Runnable) () -> {
                    try {
                        List<CoreTable> newBizInfo = sequenceDao.getAllBizInfo();
                        Set<BizInstance> newBizInstance = Sets.newConcurrentHashSet();
                        //get new all bizInstance
                        buildBizInstance(newBizInstance, newBizInfo);
                        Sets.SetView<BizInstance> union = Sets.union(allBizInstance, newBizInstance);
                        allBizInstance = union.immutableCopy();
                    } catch (Throwable e) {
                        log.info("some thing error {}", e);
                    } finally {

                    }

                }, 60, 60, TimeUnit.SECONDS);
    }

}
