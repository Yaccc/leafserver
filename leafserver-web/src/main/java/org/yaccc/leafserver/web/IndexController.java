package org.yaccc.leafserver.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yaccc.leafserver.core.SequenceCore;
import org.yaccc.leafserver.persistent.SequenceDao;

/**
 * Created by xiezhaodong  on 2018/2/11
 */
@Controller
@Slf4j
public class IndexController {
    @Autowired
    SequenceDao sequenceDao;

    @Autowired
    SequenceCore sequenceCore;

    @RequestMapping("/index")
    @ResponseBody
    String home(String app, String key) {
        synchronized (sequenceDao) {
            String re = sequenceCore.nextValue(app, key) + "=--===" + System.currentTimeMillis();
            // log.info(re);
            System.out.println(re);
        }

        return "";
    }


}
