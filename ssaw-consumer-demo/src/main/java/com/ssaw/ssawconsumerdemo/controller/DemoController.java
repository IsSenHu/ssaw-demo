package com.ssaw.ssawconsumerdemo.controller;

import com.ssaw.ssawconsumerdemo.feign.DemoFeign;
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

    private final DemoFeign demoFeign;

    @Autowired
    public DemoController(DemoFeign demoFeign) {
        this.demoFeign = demoFeign;
    }

    @GetMapping("/get")
    public Map get() {
        return demoFeign.get();
    }
}
