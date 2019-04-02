package com.ssaw.smscenter.controller;

import com.ssaw.smscenter.message.MessageVO;
import com.ssaw.smscenter.producer.TransactionProducer;
import com.ssaw.smscenter.task.NoticeZhouYinPingJianSheYinHangKaTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.data.annotation.Id;
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

    @Id
    private String id;

    private final TransactionProducer transactionProducer;

    private final NoticeZhouYinPingJianSheYinHangKaTask task;

    private MessageSource messageSource;

    @Autowired
    public Controller(NoticeZhouYinPingJianSheYinHangKaTask task, TransactionProducer transactionProducer) {
        this.task = task;
        this.transactionProducer = transactionProducer;
    }

    @GetMapping("/me/{me}/{stop}")
    public Boolean change(@PathVariable(name = "me") Boolean me, @PathVariable(name = "stop") Boolean stop) {
        task.me = me;
        task.stop = stop;
        log.info("信息通知人员我:{},启用:{}", me, stop);
        return me;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/me/sendMessage")
    public SendResult send(MessageVO messageVO) {
        log.info("messageSource:{}", messageSource);
        return transactionProducer.send(messageVO);
    }
}
