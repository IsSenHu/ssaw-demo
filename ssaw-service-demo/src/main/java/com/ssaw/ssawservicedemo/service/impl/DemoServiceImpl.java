package com.ssaw.ssawservicedemo.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ssaw.ssawservicedemo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author HuSen.
 * @date 2018/11/12 0:07.
 */
@Service
@Slf4j
public class DemoServiceImpl implements DemoService {

    @Override
    @HystrixCommand(fallbackMethod = "getFallBack", commandKey = "demoKey")
    public Map<String, String> get() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Thread.sleep(new Random().nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, String> result = new HashMap<>(1);
        result.put("key", "demo");

        log.info("调用花费时间:{}ms", stopWatch.getTime());
        return result;
    }

    @SuppressWarnings("unused")
    public Map<String, String> getFallBack() {
        Map<String, String> result = new HashMap<>(1);
        result.put("key", "demoFallBack");
        return result;
    }
}
