package org.yaccc.leafserver.web;

import com.alibaba.fastjson.JSON;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yaccc.leafserver.common.Result;
import org.yaccc.leafserver.core.SequenceCore;
import org.yaccc.leafserver.persistent.SequenceDao;

import javax.annotation.PostConstruct;
import java.util.Deque;
import java.util.Map;

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
        sequenceCore.nextValue(app, key);
        return "";
    }

    @PostConstruct
    public void init() {
        Undertow server = Undertow.builder()
                .addHttpListener(9999, "localhost").setHandler(new PathHandler() {
                    {
                        addExactPath("/id", exchange -> {
                            Map<String, Deque<String>> pathParameters = exchange.getQueryParameters();
                            Result result = sequenceCore.nextValue(pathParameters.get("app").getFirst(), pathParameters.get("key").getFirst());
                            exchange.getResponseSender().send(JSON.toJSONString(result));
                        });
                    }

                })
                .build();
        server.start();
    }






}
