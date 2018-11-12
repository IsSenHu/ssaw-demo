package com.ssaw.ssawconsumerdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen.
 * @date 2018/11/12 0:37.
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    private final RestTemplate restTemplate;

    @Autowired
    public DemoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/get")
    public Map get() {
        return restTemplate.getForObject("http://SSAW-DEMO-SERVICE/demo/get", Map.class, new HashMap<>());
    }
}
