package org.yaccc.leafserver.persistent.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaccc.leafserver.common.Segment;
import org.yaccc.leafserver.persistent.SequenceDao;
import org.yaccc.leafserver.persistent.model.CoreTable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiezhaodong  on 2018/2/23
 */
@Service
@Slf4j
public class SequenceService {
    @Resource(name = "txManager")
    private PlatformTransactionManager transactionManager;
    @Autowired
    private SequenceDao sequenceDao;

    private TransactionTemplate transactionTemplate = null;

    @PostConstruct
    public void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public Segment buildSegment(@NonNull String appName, @NonNull String key) {

        // get new segment data from database
        return transactionTemplate.execute(status -> {

            int success0 = sequenceDao.updateMaxId(appName, key);
            CoreTable oneBizInfo = sequenceDao.getOneBizInfo(appName, key);
            if (success0 < 0 || oneBizInfo == null) {
                status.setRollbackOnly();
                log.error("transaction rollback !!!!,update SQL execute status is {}. and bizInfo is {}", success0, oneBizInfo);
                return null;
            }
            log.info("alloc [{},{}] new segment,maxid-{},minid-{},step-{}", appName, key, oneBizInfo.getNowMaxId(), oneBizInfo.getNowMaxId() - oneBizInfo.getStep(), oneBizInfo.getStep());
            Segment segment = Segment.builder()//[min,max-1]
                    .min(oneBizInfo.getNowMaxId() - oneBizInfo.getStep())
                    .max(oneBizInfo.getNowMaxId() - 1)
                    .step(oneBizInfo.getStep())
                    .build();
            segment.setInitCompleted(true);
            segment.setLongFactory(new AtomicLong(segment.getMin()));
            return segment;

        });


    }

}
