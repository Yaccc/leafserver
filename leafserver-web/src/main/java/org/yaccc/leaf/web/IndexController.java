package org.yaccc.leaf.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yaccc.leaf.persistent.SequenceDao;

/**
 * Created by xiezhaodong  on 2018/2/11
 */
@Controller
public class IndexController {
    @Autowired
    SequenceDao sequenceDao;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!"+sequenceDao.test();
    }
}
