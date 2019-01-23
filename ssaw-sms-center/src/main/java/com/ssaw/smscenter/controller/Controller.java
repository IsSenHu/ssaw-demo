package com.ssaw.smscenter.controller;

import com.ssaw.smscenter.task.NoticeZhouYinPingJianSheYinHangKaTask;
import com.ssaw.ssawinterface.dubbo.test.TestApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen.
 * @date 2019/1/13 4:42.
 */
@Slf4j
@RestController
public class Controller implements MessageSourceAware {

    private final TestApiService testApiService;

    private final NoticeZhouYinPingJianSheYinHangKaTask task;

    private MessageSource messageSource;

    @Autowired
    public Controller(NoticeZhouYinPingJianSheYinHangKaTask task, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") TestApiService testApiService) {
        this.task = task;
        this.testApiService = testApiService;
    }

    @GetMapping("/me/{me}/{stop}")
    public Boolean change(@PathVariable(name = "me") Boolean me, @PathVariable(name = "stop") Boolean stop) {
        task.me = me;
        task.stop = stop;
        log.info("信息通知人员我:{},启用:{}", me, stop);
        return me;
    }

    @GetMapping("/say/{name}")
    public String sayHello(@PathVariable(name = "name") String name) {
        log.info("MessageSource:{}", messageSource);
        return testApiService.sayHello(name);
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
