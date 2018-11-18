package com.ssaw.ssawservicedemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen.
 * @date 2018/11/17 17:07.
 */
@RefreshScope
@RestController
@RequestMapping("/from")
public class ValueController {

    @Value("${from}")
    private String value;

    @GetMapping
    public String get() {
        return this.value;
    }
}
