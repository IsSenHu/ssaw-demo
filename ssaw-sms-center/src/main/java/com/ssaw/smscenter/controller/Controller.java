package com.ssaw.smscenter.controller;

import com.ssaw.smscenter.task.NoticeZhouYinPingJianSheYinHangKaTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen.
 * @date 2019/1/13 4:42.
 */
@Slf4j
@RestController
public class Controller {

    private final NoticeZhouYinPingJianSheYinHangKaTask task;

    @Autowired
    public Controller(NoticeZhouYinPingJianSheYinHangKaTask task) {
        this.task = task;
    }

    @GetMapping("/me/{me}/{stop}")
    public Boolean change(@PathVariable(name = "me") Boolean me, @PathVariable(name = "stop") Boolean stop) {
        task.me = me;
        task.stop = stop;
        log.info("信息通知人员我:{},启用:{}", me, stop);
        return me;
    }
}
